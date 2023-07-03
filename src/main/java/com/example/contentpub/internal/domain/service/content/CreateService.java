package com.example.contentpub.internal.domain.service.content;

import com.example.contentpub.internal.domain.constant.ContentAction;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;
import com.example.contentpub.internal.external.entity.Category;
import com.example.contentpub.internal.external.entity.Content;
import com.example.contentpub.internal.external.entity.ContentDetails;
import com.example.contentpub.internal.external.entity.Writer;
import com.example.contentpub.internal.domain.dto.messaging.MessageDto;
import com.example.contentpub.internal.domain.exception.DomainException;
import com.example.contentpub.internal.domain.boundary.repository.CategoryRepository;
import com.example.contentpub.internal.domain.boundary.repository.ContentDetailsRepository;
import com.example.contentpub.internal.domain.boundary.repository.ContentRepository;
import com.example.contentpub.internal.domain.boundary.repository.WriterRepository;
import com.example.contentpub.internal.domain.service.messaging.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.example.contentpub.internal.domain.constant.DomainConstants.FAILURE;
import static com.example.contentpub.internal.domain.constant.DomainConstants.SUCCESS;
import static com.example.contentpub.internal.domain.constant.ErrorCode.*;

@Service("CREATE")
public class CreateService implements ContentService {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ContentDetailsRepository contentDetailsRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public CommonDomainResponse<String> process(ContentDomainRequest domainRequest) {

        CommonDomainResponse<String> response = new CommonDomainResponse<>();

        try {

            Integer writerId = writerRepository.findWriterId(domainRequest.getUserId());

            if (writerId == null) { // If the user is not registered as a writer, return an error response.
                throw new DomainException(WRITER_NOT_REGISTERED);
            }

            Writer writer = new Writer();
            writer.setId(writerId);

            Content contentToBeSaved = new Content();
            contentToBeSaved.setTitle(domainRequest.getTitle());
            contentToBeSaved.setWriter(writer);

            Category category = categoryRepository.findById(domainRequest.getCategoryId())
                    .orElseThrow(() -> new DomainException(CATEGORY_NOT_FOUND));
            contentToBeSaved.setCategory(category);

            String contentCategory = category.getName();

            ContentDetails contentDetails = new ContentDetails();
            contentDetails.setContent(contentToBeSaved);
            contentDetails.setSummary(domainRequest.getSummary());
            contentDetails.setDetails(domainRequest.getDetails());
            contentToBeSaved.setContentDetails(contentDetails);

            contentDetailsRepository.save(contentDetails);
            Content savedContent = contentRepository.save(contentToBeSaved);
            Integer contentId = savedContent.getId();

            response.setStatusCode(HttpStatus.CREATED.value());
            response.setStatus(SUCCESS);

            /* Prepare and send message for notification. */
            MessageDto message = MessageDto.builder()
                    .category(contentCategory)
                    .contentId(contentId)
                    .actionType(ContentAction.CREATE).build();
            messageUtil.prepareAndSendMessage(message);

        } catch (DomainException ex) {
            response.setStatusCode(ex.getHttpStatusCode());
            response.setStatus(FAILURE);
            response.setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setStatus(FAILURE);
        }

        return response;
    }
}
