package com.example.contentpub.internal.application.controller;

import com.example.contentpub.internal.application.dto.response.CommonApiResponse;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.view.ContentListView;
import com.example.contentpub.internal.domain.dto.view.ContentItemView;
import com.example.contentpub.internal.domain.dto.view.ViewDomainRequest;
import com.example.contentpub.internal.domain.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.contentpub.internal.application.constant.ParamConstants.*;

/**
 * The ViewController contains the Read method related APIs relevant to content.
 */
@RestController
@RequestMapping("/view")
public class ViewController {

    @Autowired
    private ViewService viewService;

    /**
     * View a list of content according to search criteria. Does not contain the content details of the fetched content.
     * @param categoryId the category ID of the articles.
     * @param page the index of the page as request parameter.
     * @param pageSize the pageSize as request parameter.
     * @return the list of content matching the criteria as request parameter.
     */
    @GetMapping("/content")
    public ResponseEntity<CommonApiResponse<ContentListView>> getContentList(@RequestParam(name = CATEGORY_ID) Integer categoryId,
                                                                          @RequestParam(name = PAGE) Integer page,
                                                                          @RequestParam(name = PAGE_SIZE) Integer pageSize) {

        ViewDomainRequest requestEntity = ViewDomainRequest.builder()
                .categoryId(categoryId)
                .page(page)
                .pageSize(pageSize)
                .build();

        CommonDomainResponse<ContentListView> domainResponse = viewService.getContentList(requestEntity);

        return ResponseEntity.status(domainResponse.getStatusCode())
                .body(new CommonApiResponse<>(domainResponse.getStatus(), domainResponse.getDescription()));
    }

    /**
     * Fetch all the relevant details of single content.
     * @param contentId the ID of the content as a URL path variable.
     * @return the details of the content.
     */
    @GetMapping("/content/{id}")
    public ResponseEntity<CommonApiResponse<ContentItemView>> getSingleContentItem(@PathVariable("id") Integer contentId) {

        ViewDomainRequest requestEntity = ViewDomainRequest.builder().contentId(contentId).build();

        CommonDomainResponse<ContentItemView> domainResponse = viewService.getSingleContentItem(requestEntity);

        return ResponseEntity.status(domainResponse.getStatusCode())
                .body(new CommonApiResponse<>(domainResponse.getStatus(), domainResponse.getDescription()));
    }

}
