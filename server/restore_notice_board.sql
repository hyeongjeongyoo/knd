-- 공지사항 게시판 데이터 복구

-- 1. 현재 상태 확인
SELECT 'menu' as table_name, id, name, type
FROM menu
WHERE
    name = '공지사항';

SELECT
    'bbs_master' as table_name,
    BBS_ID,
    BBS_NAME,
    SKIN_TYPE
FROM bbs_master
WHERE
    BBS_ID = 1;

-- 2. 공지사항 메뉴 복구 (ID=1)
INSERT INTO
    menu (
        id,
        name,
        type,
        url,
        display_position,
        visible,
        sort_order,
        created_at,
        updated_at
    )
VALUES (
        1,
        '공지사항',
        'BOARD',
        '/bbs/notice',
        'HEADER',
        1,
        1,
        NOW(),
        NOW()
    )
ON DUPLICATE KEY UPDATE
    name = '공지사항',
    type = 'BOARD',
    url = '/bbs/notice',
    updated_at = NOW();

-- 3. 공지사항 게시판 마스터 복구 (BBS_ID=1)
INSERT INTO
    bbs_master (
        BBS_ID,
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
        1,
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
    )
ON DUPLICATE KEY UPDATE
    BBS_NAME = '공지사항',
    SKIN_TYPE = 'BASIC',
    READ_AUTH = 'ALL',
    WRITE_AUTH = 'ADMIN',
    ADMIN_AUTH = 'ADMIN',
    DISPLAY_YN = 'Y',
    ATTACHMENT_YN = 'Y',
    ATTACHMENT_LIMIT = 5,
    ATTACHMENT_SIZE = 20,
    UPDATED_AT = NOW();

-- 4. 복구 결과 확인
SELECT
    'AFTER_INSERT' as status,
    'menu' as table_name,
    id,
    name,
    type
FROM menu
WHERE
    id = 1;

SELECT
    'AFTER_INSERT' as status,
    'bbs_master' as table_name,
    BBS_ID,
    BBS_NAME,
    SKIN_TYPE
FROM bbs_master
WHERE
    BBS_ID = 1;