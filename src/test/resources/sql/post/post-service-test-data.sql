
insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'dev.hyoseung@gmail.com', 'hyoseung', 'Suwon', 'aaaaaaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ACTIVE', 0);

insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (2, 'jaeyeon@gmail.com', 'jaeyeon', 'Suwon', 'aaaaaaaa-aaaa-aaaa-aaaaaaaaaaab', 'PENDING', 0);

insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (1, 'hello world', 1678350673958, 0, 1);
