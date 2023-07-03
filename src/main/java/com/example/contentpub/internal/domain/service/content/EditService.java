package com.example.contentpub.internal.domain.service.content;

import com.example.contentpub.internal.domain.constant.ContentAction;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;
import com.example.contentpub.internal.external.entity.Content;
import com.example.contentpub.internal.domain.dto.messaging.MessageDto;
import com.example.contentpub.internal.domain.exception.DomainException;
import com.example.contentpub.internal.domain.boundary.repository.ContentRepository;
import com.example.contentpub.internal.domain.service.messaging.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.example.contentpub.internal.domain.constant.DomainConstants.FAILURE;
import static com.example.contentpub.internal.domain.constant.DomainConstants.SUCCESS;
import static com.example.contentpub.internal.domain.constant.ErrorCode.CONTENT_NOT_BELONG_TO_USER;
import static com.example.contentpub.internal.domain.constant.ErrorCode.CONTENT_NOT_FOUND;

@Service("EDIT")
public class EditService implements ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public CommonDomainResponse<String> process(ContentDomainRequest domainRequest) {

        CommonDomainResponse<String> response = new CommonDomainResponse<>();

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
                contentToBeEdited.getContentDetails().setSummary(domainRequest.getSummary());
            }
            if (domainRequest.getDetails() != null) {
                contentToBeEdited.getContentDetails().setDetails(domainRequest.getDetails());
            }

            String contentCategory = contentToBeEdited.getCategory().getName();

            contentRepository.save(contentToBeEdited);

            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus(SUCCESS);

            /* Prepare and send message for notification. */
            MessageDto message = MessageDto.builder()
                    .category(contentCategory)
                    .contentId(domainRequest.getContentId())
                    .actionType(ContentAction.EDIT).build();
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
