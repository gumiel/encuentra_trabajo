package com.gumiel.perez.tareadiplomado.data;

import android.provider.BaseColumns;

/**
 * Created by henry on 20/10/2015.
 */
public class JobPostDbContract {
    public static class JobPost implements BaseColumns {

        public static final String TABLE_NAME = "job_posts";


        public static final String TITLE_COLUMN = "title";
        public static final String DESCRIPTION_COLUMN = "description";
        public static final String POSTED_DATE_COLUMN = "posted_date";

    }
}
