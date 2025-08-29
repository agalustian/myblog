create table if not exists posts
(
    id         text primary key,
    title      varchar(256) not null,
    text       text         not null,
    user_id    varchar(256) not null,
    created_at timestamp    not null default now(),
    updated_at timestamp,
-- TODO use varchar Array instead
    tags       text         not null
);

-- create unique index on posts ("title", "user_id");

insert into posts (id, title, text, user_id, tags)
values ('726f8caf-366a-4f2d-a5a4-b7ebd4310e9c', 'Awesome post 1',
        'BUILD SUCCESSFUL in 659ms
   3 actionable tasks: 2 executed, 1 up-to-date
   Configuration cache entry reused.
   14:51:56: Execution finished',
        'Иванов',
        'awesome first'),
       ('944630a6-2a6c-42d5-bda8-7fb8dc4895bf', 'Awesome post 2',
        'BUILD SUCCESSFUL in 659ms
   3 actionable tasks: 2 executed, 1 up-to-date
   Configuration cache entry reused.
   14:51:56: Execution finished',
        'Иванов',
        'awesome,second'),
       ('726f8caf-366a-4f2d-a5a4-b6ebd4310e9c', 'Awesome post 1',
        'BUILD SUCCESSFUL in 659ms
   3 actionable tasks: 2 executed, 1 up-to-date
   Configuration cache entry reused.
   14:51:56: Execution finished',
        'Иванов',
        'awesome first'),
       ('944630a6-2a6c-42d5-bda8-7fb8dc4795bf', 'Awesome post 2',
        'BUILD SUCCESSFUL in 659ms
   3 actionable tasks: 2 executed, 1 up-to-date
   Configuration cache entry reused.
   14:51:56: Execution finished',
        'Иванов',
        'awesome second'),
       ('726f8caf-366a-4f2d-a5a4-b7ebd4311e9c', 'Awesome post 1',
        'BUILD SUCCESSFUL in 659ms
   3 actionable tasks: 2 executed, 1 up-to-date
   Configuration cache entry reused.
   14:51:56: Execution finished',
        'Иванов',
        'awesome first'),
       ('944630a6-2a6c-42d5-bda8-7fb8dc4894bf', 'Awesome post 2',
        'BUILD SUCCESSFUL in 659ms
   3 actionable tasks: 2 executed, 1 up-to-date
   Configuration cache entry reused.
   14:51:56: Execution finished',
        'Иванов',
        'awesome second'),
       ('706636c3-6c24-4741-9e7d-ac49066d42ad', 'Awesome post 3',
        'BUILD SUCCESSFUL in 659ms
   3 actionable tasks: 2 executed, 1 up-to-date
   Configuration cache entry reused.
   14:51:56: Execution finished',
        'Сидоров',
        'awesome third');

create table if not exists post_images
(
-- TODO add references to posts
    id    text primary key,
    image bytea not null
);

create table if not exists post_likes
(
    post_id text,
    user_id text
);
-- create unique index on post_likes ("post_id", "user_id");

create table if not exists post_comments
(
-- TODO add references to posts
    id         text primary key,
    post_id    text,
    user_id    text,
    comment    text,
    created_at timestamp not null default now(),
    updated_at timestamp
);

insert into post_comments (id, post_id, user_id, comment)
values ('726f8caf-366a-4f2d-a5a4-b7ebd4390e9c',
        '726f8caf-366a-4f2d-a5a4-b7ebd4310e9c',
        'Иванов',
        'Awesome great comment!'),
       ('726f8caf-366a-4f2d-a5a4-b7ebd1390e9c',
        '726f8caf-366a-4f2d-a5a4-b7ebd4310e9c',
        'Сидоров',
        'Another awesome great comment!'),
       ('944630a6-2a6c-42d5-bda8-7fb8dc4195bf',
        '944630a6-2a6c-42d5-bda8-7fb8dc4895bf',
        'Иванов',
        'Second awesome comment!');