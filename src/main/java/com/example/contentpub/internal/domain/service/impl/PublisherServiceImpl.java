package com.example.contentpub.internal.domain.service.impl;

import com.example.contentpub.internal.domain.boundary.repository.CountryRepository;
import com.example.contentpub.internal.domain.boundary.repository.UserRepository;
import com.example.contentpub.internal.domain.boundary.repository.WriterDescriptionRepository;
import com.example.contentpub.internal.domain.boundary.repository.WriterRepository;
import com.example.contentpub.internal.domain.constant.ErrorCode;
import com.example.contentpub.internal.domain.constant.Role;
import com.example.contentpub.internal.domain.constant.StatusCode;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse2;
import com.example.contentpub.internal.domain.dto.publish.CreatedWriter;
import com.example.contentpub.internal.domain.dto.publish.DomainPublisherRequest;
import com.example.contentpub.internal.domain.exception.DomainException;
import com.example.contentpub.internal.domain.service.interfaces.PublisherService;
import com.example.contentpub.internal.external.entity.Country;
import com.example.contentpub.internal.external.entity.User;
import com.example.contentpub.internal.external.entity.Writer;
import com.example.contentpub.internal.external.entity.WriterDescription;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final WriterRepository writerRepository;

    private final UserRepository userRepository;

    private final CountryRepository countryRepository;

    private final WriterDescriptionRepository writerDescriptionRepository;

    public PublisherServiceImpl(WriterRepository writerRepository, UserRepository userRepository,
                                CountryRepository countryRepository,
                                WriterDescriptionRepository writerDescriptionRepository) {
        this.writerRepository = writerRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.writerDescriptionRepository = writerDescriptionRepository;
    }

    @Override
    public CommonDomainResponse2<CreatedWriter> createPublisher(DomainPublisherRequest createPublisherRequest) {

        CommonDomainResponse2<CreatedWriter> response = new CommonDomainResponse2<>();

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
            Writer savedWriter = writerRepository.save(writerToBeSaved);

            response.setHttpStatusCode(StatusCode.CREATED.getHttpStatus().value());
            response.setCode(StatusCode.CREATED.getCode());
            response.setDescription(StatusCode.CREATED.getDescription());
            response.setData(new CreatedWriter(savedWriter.getId()));

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
