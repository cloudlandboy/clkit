-- 用户,密码都是 clkit
INSERT INTO CLKIT_USER(id, created_time, updated_time, email, name, nickname, password, real_name)
VALUES (1, NOW(), NOW(), '', 'clkit', '管理员',
        '$2a$10$9KWv4OcFNyR9cHT68s.gy.Evl7RCQZHiL5ClKUDmiFtT5Ege//ZuK', '管理员');
INSERT INTO CLKIT_USER(id, created_time, updated_time, email, name, nickname, password, real_name)
VALUES (2, NOW(), NOW(), '', 'guest', '游客',
        '$2a$10$9KWv4OcFNyR9cHT68s.gy.Evl7RCQZHiL5ClKUDmiFtT5Ege//ZuK', '游客');

-- 角色
INSERT INTO ROLE(id, created_time, updated_time, code, name)
VALUES (1, NOW(), NOW(), 'ROLE_ADMIN', '管理员');

-- 用户角色
INSERT INTO CLKIT_USER_ROLE(clkit_user_id, role_id)
VALUES (1, 1);

-- 推荐扩展
INSERT INTO EXTENSION(name, url, path, type, sort_value, folder_id, hide, created_time, updated_time)
VALUES ('中国色', 'https://www.zhongguose.com', 'https://www.zhongguose.com', '1', 0, 0, false, NOW(), NOW())