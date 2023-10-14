package com.example.contentpub.internal.domain.service.impl.content;

import com.example.contentpub.internal.domain.boundary.repository.CategoryRepository;
import com.example.contentpub.internal.domain.boundary.repository.ContentDetailsRepository;
import com.example.contentpub.internal.domain.boundary.repository.ContentRepository;
import com.example.contentpub.internal.domain.boundary.repository.WriterRepository;
import com.example.contentpub.internal.domain.constant.ContentAction;
import com.example.contentpub.internal.domain.constant.StatusCode;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse2;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;
import com.example.contentpub.internal.domain.dto.messaging.MessageDto;
import com.example.contentpub.internal.domain.dto.publish.CreatedContent;
import com.example.contentpub.internal.domain.exception.DomainException;
import com.example.contentpub.internal.domain.service.interfaces.content.ContentService;
import com.example.contentpub.internal.domain.service.interfaces.messaging.MessageService;
import com.example.contentpub.internal.external.entity.Category;
import com.example.contentpub.internal.external.entity.Content;
import com.example.contentpub.internal.external.entity.ContentDetails;
import com.example.contentpub.internal.external.entity.Writer;
import org.springframework.stereotype.Service;

import static com.example.contentpub.internal.domain.constant.ErrorCode.*;

@Service
public class ContentServiceImpl implements ContentService {

    private final WriterRepository writerRepository;

    private final CategoryRepository categoryRepository;

    private final ContentDetailsRepository contentDetailsRepository;

    private final ContentRepository contentRepository;

    private final MessageService messageService;

    public ContentServiceImpl(WriterRepository writerRepository, CategoryRepository categoryRepository,
                              ContentDetailsRepository contentDetailsRepository, ContentRepository contentRepository,
                              MessageService messageService) {
        this.writerRepository = writerRepository;
        this.categoryRepository = categoryRepository;
        this.contentDetailsRepository = contentDetailsRepository;
        this.contentRepository = contentRepository;
        this.messageService = messageService;
    }

    @Override
    public CommonDomainResponse2<CreatedContent> createContent(ContentDomainRequest domainRequest) {

        CommonDomainResponse2<CreatedContent> response = new CommonDomainResponse2<>();

        try {

            Integer writerId = writerRepository.findWriterId(domainRequest.getUserId());

            if (writerId == null) { // If the user is not registered as a writer, return an error response.
                throw new DomainException(WRITER_NOT_REGISTERED);
            }

            Writer writer = new Writer();
            writer.setId(writerId);

            Content contentToBeSaved = new Content();
            contentToBeSaved.setTitle(domainRequest.getTitle());
            contentToBeSaved.setSummary(domainRequest.getSummary());
            contentToBeSaved.setWriter(writer);

            Category category = categoryRepository.findById(domainRequest.getCategoryId())
                    .orElseThrow(() -> new DomainException(CATEGORY_NOT_FOUND));
            contentToBeSaved.setCategory(category);

            String contentCategory = category.getName();

            ContentDetails contentDetails = new ContentDetails();
            contentDetails.setContent(contentToBeSaved);
            contentDetails.setDetails(domainRequest.getDetails());
            contentToBeSaved.setContentDetails(contentDetails);

            contentDetailsRepository.save(contentDetails);
            Content savedContent = contentRepository.save(contentToBeSaved);
            Integer contentId = savedContent.getId();

            response.setHttpStatusCode(StatusCode.CREATED.getHttpStatus().value());
            response.setCode(StatusCode.CREATED.getCode());
            response.setDescription(StatusCode.CREATED.getDescription());
            response.setData(new CreatedContent(contentId));

            /* Prepare and send message for notification. */
            MessageDto message = MessageDto.builder()
                    .category(contentCategory)
                    .contentId(contentId)
                    .actionType(ContentAction.CREATE).build();
            messageService.prepareAndSendMessage(message);

        } catch (DomainException ex) {
            response.setHttpStatusCode(ex.getHttpStatusCode());
            response.setCode(ex.getCode());
            response.setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setHttpStatusCode(StatusCode.INTERNAL_ERROR.getHttpStatus().value());
            response.setCode(StatusCode.INTERNAL_ERROR.getCode());
            response.setDescription(StatusCode.INTERNAL_ERROR.getDescription());
        }

        return response;
    }

    @Override
    public CommonDomainResponse2<String> editContent(ContentDomainRequest domainRequest) {

        CommonDomainResponse2<String> response = new CommonDomainResponse2<>();

        try {
            Content contentToBeEdited = contentRepository.findById(domainRequest.getContentId())
                    .orElseThrow(() -> new DomainException(CONTENT_NOT_FOUND));

            if (!contentToBeEdited.getWriter().getUser().getId()
                    .equals(domainRequest.getUserId())) { // Only the owner can modify/delete content.
                throw new DomainException(CONTENT_NOT_BELONG_TO_USER);
            }

            if (domainRequest.getTitle() != null) {
                contentToBeEdited.setTitle(domainRequest.getTitle());
            }
            if (domainRequest.getSummary() != null) {
                contentToBeEdited.setSummary(domainRequest.getSummary());
            }
            if (domainRequest.getDetails() != null) {
                contentToBeEdited.getContentDetails().setDetails(domainRequest.getDetails());
            }

            String contentCategory = contentToBeEdited.getCategory().getName();

            contentRepository.save(contentToBeEdited);

            response.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
            response.setCode(StatusCode.SUCCESS.getCode());
            response.setDescription(StatusCode.SUCCESS.getDescription());

            /* Prepare and send message for notification. */
            MessageDto message = MessageDto.builder()
                    .category(contentCategory)
                    .contentId(domainRequest.getContentId())
                    .actionType(ContentAction.EDIT).build();
            messageService.prepareAndSendMessage(message);

        } catch (DomainException ex) {
            response.setHttpStatusCode(ex.getHttpStatusCode());
            response.setCode(ex.getCode());
            response.setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setHttpStatusCode(StatusCode.INTERNAL_ERROR.getHttpStatus().value());
            response.setCode(StatusCode.INTERNAL_ERROR.getCode());
            response.setDescription(StatusCode.INTERNAL_ERROR.getDescription());
        }

        return response;
    }

    @Override
    public CommonDomainResponse2<String> deleteContent(ContentDomainRequest domainRequest) {

        CommonDomainResponse2<String> response = new CommonDomainResponse2<>();

        try {
            Content contentToBeDeleted = contentRepository.findById(domainRequest.getContentId())
                    .orElseThrow(() -> new DomainException(CONTENT_NOT_FOUND));

            if (!contentToBeDeleted.getWriter().getUser().getId()
                    .equals(domainRequest.getUserId())) { // Only the owner can modify/delete content.
                throw new DomainException(CONTENT_NOT_BELONG_TO_USER);
            }

            String contentCategory = contentToBeDeleted.getCategory().getName();

            contentRepository.delete(contentToBeDeleted.getId());

            response.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
            response.setCode(StatusCode.SUCCESS.getCode());
            response.setDescription(StatusCode.SUCCESS.getDescription());

            /* Prepare and send message for notification. */
            MessageDto message = MessageDto.builder()
                    .category(contentCategory)
                    .contentId(domainRequest.getContentId())
                    .actionType(ContentAction.DELETE).build();
            messageService.prepareAndSendMessage(message);

        } catch (DomainException ex) {
            response.setHttpStatusCode(ex.getHttpStatusCode());
            response.setCode(ex.getCode());
            response.setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setHttpStatusCode(StatusCode.INTERNAL_ERROR.getHttpStatus().value());
            response.setCode(StatusCode.INTERNAL_ERROR.getCode());
            response.setDescription(StatusCode.INTERNAL_ERROR.getDescription());
        }

        return response;
    }
}
