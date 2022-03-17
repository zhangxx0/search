package com.xinxin.search.gettingstarted;

import lombok.Builder;
import lombok.Data;


/**
 *
 * @author hanko
 */
@Data
@Builder
public class Blog {
    private String id;
    private String title;
}
