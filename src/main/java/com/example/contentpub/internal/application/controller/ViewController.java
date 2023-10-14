package com.example.contentpub.internal.application.controller;

import com.example.contentpub.internal.application.dto.response.CommonApiResponse2;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse2;
import com.example.contentpub.internal.domain.dto.view.ContentItemView;
import com.example.contentpub.internal.domain.dto.view.ContentListView;
import com.example.contentpub.internal.domain.dto.view.ViewDomainRequest;
import com.example.contentpub.internal.domain.service.impl.ViewServiceImpl;
import com.example.contentpub.internal.domain.service.interfaces.ViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.contentpub.internal.application.constant.ParamConstants.*;

/**
 * The ViewController contains the Read method related APIs relevant to content.
 */
@RestController
@RequestMapping("/view")
public class ViewController {

    private final ViewService viewService;

    public ViewController(ViewServiceImpl viewService) {
        this.viewService = viewService;
    }

    /**
     * View a list of content according to search criteria. Does not contain the content details of the fetched content.
     *
     * @param categoryId the category ID of the articles.
     * @param page       the index of the page as request parameter.
     * @param pageSize   the pageSize as request parameter.
     * @return the list of content matching the criteria as request parameter.
     */
    @GetMapping("/content")
    public ResponseEntity<CommonApiResponse2<ContentListView>> getContentList(
            @RequestParam(name = CATEGORY_ID) Integer categoryId, @RequestParam(name = PAGE) Integer page,
            @RequestParam(name = PAGE_SIZE) Integer pageSize) {

        ViewDomainRequest requestEntity = ViewDomainRequest.builder()
                .categoryId(categoryId)
                .page(page)
                .pageSize(pageSize)
                .build();

        CommonDomainResponse2<ContentListView> domainResponse = viewService.getContentList(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(new CommonApiResponse2<>(domainResponse.getCode(), domainResponse.getDescription(), domainResponse.getData()));
    }

    /**
     * Fetch all the relevant details of single content.
     *
     * @param contentId the ID of the content as a URL path variable.
     * @return the details of the content.
     */
    @GetMapping("/content/{id}")
    public ResponseEntity<CommonApiResponse2<ContentItemView>> getSingleContentItem(
            @PathVariable("id") Integer contentId) {

        ViewDomainRequest requestEntity = ViewDomainRequest.builder().contentId(contentId).build();

        CommonDomainResponse2<ContentItemView> domainResponse = viewService.getSingleContentItem(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(new CommonApiResponse2<>(domainResponse.getCode(), domainResponse.getDescription(), domainResponse.getData()));
    }

}
