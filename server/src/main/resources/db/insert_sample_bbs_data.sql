-- 실제 사용하는 게시판 마스터 데이터 삽입

-- 1. 공지사항 게시판
INSERT INTO
    bbs_master (
        BBS_NAME,
        SKIN_TYPE,
        READ_AUTH,
        WRITE_AUTH,
        ADMIN_AUTH,
        DISPLAY_YN,
        SORT_ORDER,
        NOTICE_YN,
        PUBLISH_YN,
        ATTACHMENT_YN,
        ATTACHMENT_LIMIT,
        ATTACHMENT_SIZE,
        CREATED_AT,
        UPDATED_AT
    )
VALUES (
        '공지사항',
        'BASIC',
        'ALL',
        'ADMIN',
        'ADMIN',
        'Y',
        'D',
        'Y',
        'Y',
        'Y',
        5,
        20,
        NOW(),
        NOW()
    );

-- 2. 뉴스/보도자료 게시판
INSERT INTO
    bbs_master (
        BBS_NAME,
        SKIN_TYPE,
        READ_AUTH,
        WRITE_AUTH,
        ADMIN_AUTH,
        DISPLAY_YN,
        SORT_ORDER,
        NOTICE_YN,
        PUBLISH_YN,
        ATTACHMENT_YN,
        ATTACHMENT_LIMIT,
        ATTACHMENT_SIZE,
        CREATED_AT,
        UPDATED_AT
    )
VALUES (
        '뉴스/보도자료',
        'PRESS',
        'ALL',
        'ADMIN',
        'ADMIN',
        'Y',
        'D',
        'N',
        'Y',
        'Y',
        10,
        50,
        NOW(),
        NOW()
    );

-- 3. IR 게시판
INSERT INTO
    bbs_master (
        BBS_NAME,
        SKIN_TYPE,
        READ_AUTH,
        WRITE_AUTH,
        ADMIN_AUTH,
        DISPLAY_YN,
        SORT_ORDER,
        NOTICE_YN,
        PUBLISH_YN,
        ATTACHMENT_YN,
        ATTACHMENT_LIMIT,
        ATTACHMENT_SIZE,
        CREATED_AT,
        UPDATED_AT
    )
VALUES (
        'IR',
        'BASIC',
        'ALL',
        'ADMIN',
        'ADMIN',
        'Y',
        'D',
        'N',
        'Y',
        'Y',
        10,
        100,
        NOW(),
        NOW()
    );