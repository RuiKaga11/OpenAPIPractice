-- `t_user`はUserエンティティで @Table(name="t_user") と設定した場合の例です。
-- カラム名はUserエンティティのフィールドに合わせてください。

INSERT INTO t_user (id, username, email, created_at) VALUES (101, 'Taro', 'taro@example.com', '2025-09-25 09:25:00');
INSERT INTO t_user (id, username, email, created_at) VALUES (102, 'Jiro', 'jiro@example.com', '2025-10-01 10:00:00');
INSERT INTO t_user (id, username, email, created_at) VALUES (103, 'Hanako', 'hanako@example.com', '2025-10-15 11:30:00');