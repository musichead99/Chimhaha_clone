insert
into
    menu
    (name)
values
    ('침착맨');

insert
into
    boards
    (name, description, like_limit, menu_id)
values
    ('침착맨', '침착맨에 대해 이야기하는 게시판입니다.', 10, 1);

insert
into
    category
    (name, board_id)
values
    ('침착맨', 1);

insert
into
    posts
    (menu_id, board_id, category_id, title, content, popular_flag)
values
    (1, 1, 1, '테스트글', '테스트글', 'N');

insert
into
    comments
    (post_id, parent_id, content, is_deleted)
values
    (1, null, '테스트댓글', 'N'),
    (1, null, '테스트댓글', 'N'),
    (1, 1, '테스트댓글', 'N'),
    (1, 3, '테스트댓글', 'N'),
    (1, 4, '테스트댓글', 'N');