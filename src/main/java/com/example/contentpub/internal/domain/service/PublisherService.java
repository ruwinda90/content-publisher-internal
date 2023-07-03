package com.example.contentpub.internal.domain.service;

import com.example.contentpub.internal.application.dto.request.CreatePublisherRequest;
import com.example.contentpub.internal.domain.constant.ErrorCode;
import com.example.contentpub.internal.domain.constant.Role;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.external.entity.Country;
import com.example.contentpub.internal.external.entity.User;
import com.example.contentpub.internal.external.entity.Writer;
import com.example.contentpub.internal.external.entity.WriterDescription;
import com.example.contentpub.internal.domain.exception.DomainException;
import com.example.contentpub.internal.domain.boundary.repository.CountryRepository;
import com.example.contentpub.internal.domain.boundary.repository.UserRepository;
import com.example.contentpub.internal.domain.boundary.repository.WriterDescriptionRepository;
import com.example.contentpub.internal.domain.boundary.repository.WriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.example.contentpub.internal.domain.constant.DomainConstants.FAILURE;
import static com.example.contentpub.internal.domain.constant.DomainConstants.SUCCESS;

@Service
public class PublisherService {

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private WriterDescriptionRepository writerDescriptionRepository;

    public CommonDomainResponse<String> createPublisher(CreatePublisherRequest createPublisherRequest) {

        CommonDomainResponse<String> response = new CommonDomainResponse<>();

        try {

            User existingUser = userRepository.findById(createPublisherRequest.getUserId())
                    .orElseThrow(() -> new DomainException(ErrorCode.USER_NOT_FOUND));

            Writer writerToBeSaved = new Writer();
            writerToBeSaved.setName(createPublisherRequest.getName());

            Country existingCountry = countryRepository.findById(createPublisherRequest.getCountryId())
                            .orElseThrow(() -> new DomainException(ErrorCode.COUNTRY_NOT_FOUND));

            existingUser.setRole(Role.USER_WRITER);

            writerToBeSaved.setCountry(existingCountry);
            writerToBeSaved.setUser(existingUser);

            userRepository.save(existingUser);

            WriterDescription writerDescription = new WriterDescription();
            writerDescription.setWriter(writerToBeSaved);
            writerDescription.setDescription(createPublisherRequest.getDescription());
            writerToBeSaved.setWriterDescription(writerDescription);

            writerDescriptionRepository.save(writerDescription);
            writerRepository.save(writerToBeSaved);

            response.setStatusCode(HttpStatus.CREATED.value());
            response.setStatus(SUCCESS);

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
