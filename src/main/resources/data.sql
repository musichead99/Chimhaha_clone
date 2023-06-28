insert
into
    member
    (name, nickname, email, profile_image, member_role)
values
    ('정성구', 'nine', 'musichead99@naver.com', ' ', 'ADMIN');

insert
into
    menu
    (name, member_id)
values
    ('침착맨', 1);

insert
into
    boards
    (name, description, like_limit, menu_id, member_id)
values
    ('침착맨', '침착맨에 대해 이야기하는 게시판입니다.', 10, 1, 1);

insert
into
    category
    (name, board_id, member_id)
values
    ('침착맨', 1, 1);

insert
into
    posts
    (menu_id, board_id, category_id, title, content, popular_flag, member_id)
values
    (1, 1, 1, '테스트글', '테스트글', 'N', 1);

insert
into
    comments
    (post_id, parent_id, content, is_deleted, member_id)
values
    (1, null, '테스트댓글', 'N', 1),
    (1, null, '테스트댓글', 'N', 1),
    (1, 1, '테스트댓글', 'N', 1),
    (1, 3, '테스트댓글', 'N', 1),
    (1, 4, '테스트댓글', 'N', 1);