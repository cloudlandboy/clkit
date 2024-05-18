-- 用户,用户名密码都是 clkit
INSERT INTO CLKIT_USER(created_time, updated_time, email, name, nickname, password, real_name)
VALUES (NOW(), NOW(), '', 'clkit', '管理员', '$2a$10$9KWv4OcFNyR9cHT68s.gy.Evl7RCQZHiL5ClKUDmiFtT5Ege//ZuK', '管理员');

-- 角色
INSERT INTO ROLE(created_time, updated_time, code, name) values ( NOW(),NOW(),'ROLE_ADMIN','管理员' );

--