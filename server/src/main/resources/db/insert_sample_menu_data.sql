-- 샘플 메뉴 데이터 삽입

-- 최상위 메뉴들
INSERT INTO
    menu (
        name,
        type,
        url,
        display_position,
        visible,
        sort_order,
        parent_id
    )
VALUES (
        '홈',
        'LINK',
        '/',
        'HEADER',
        1,
        1,
        NULL
    ),
    (
        '회사소개',
        'FOLDER',
        NULL,
        'HEADER',
        1,
        2,
        NULL
    ),
    (
        '사업분야',
        'FOLDER',
        NULL,
        'HEADER',
        1,
        3,
        NULL
    ),
    (
        '고객지원',
        'FOLDER',
        NULL,
        'HEADER',
        1,
        4,
        NULL
    ),
    (
        '채용정보',
        'LINK',
        '/jobs',
        'HEADER',
        1,
        5,
        NULL
    );

-- 회사소개 하위 메뉴들
INSERT INTO
    menu (
        name,
        type,
        url,
        display_position,
        visible,
        sort_order,
        parent_id
    )
VALUES (
        '회사개요',
        'CONTENT',
        '/company/about',
        'HEADER',
        1,
        1,
        2
    ),
    (
        '조직도',
        'CONTENT',
        '/company/organization',
        'HEADER',
        1,
        2,
        2
    ),
    (
        '찾아오시는 길',
        'CONTENT',
        '/company/location',
        'HEADER',
        1,
        3,
        2
    );

-- 사업분야 하위 메뉴들
INSERT INTO
    menu (
        name,
        type,
        url,
        display_position,
        visible,
        sort_order,
        parent_id
    )
VALUES (
        '사업소개',
        'CONTENT',
        '/business/introduction',
        'HEADER',
        1,
        1,
        3
    ),
    (
        '제품소개',
        'CONTENT',
        '/business/products',
        'HEADER',
        1,
        2,
        3
    ),
    (
        '기술력',
        'CONTENT',
        '/business/technology',
        'HEADER',
        1,
        3,
        3
    );

-- 고객지원 하위 메뉴들
INSERT INTO
    menu (
        name,
        type,
        url,
        display_position,
        visible,
        sort_order,
        parent_id
    )
VALUES (
        '공지사항',
        'BOARD',
        '/bbs/notice',
        'HEADER',
        1,
        1,
        4
    ),
    (
        '자주하는 질문',
        'BOARD',
        '/bbs/faq',
        'HEADER',
        1,
        2,
        4
    ),
    (
        '고객의 소리',
        'BOARD',
        '/bbs/qna',
        'HEADER',
        1,
        3,
        4
    ),
    (
        '자료실',
        'BOARD',
        '/bbs/data',
        'HEADER',
        1,
        4,
        4
    );