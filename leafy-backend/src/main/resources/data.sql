
-- Users 테이블에 데이터 삽입
-- $2a$10$vYR4pPQqR/oZcUDZfXrahecEejQHY0kLkDB5s.FctPRMcEMh1PYhG = password123
-- $2a$10$Vqx3VUuB8gy9NvtKHQARWOOYB2wG4wV2WXy1sdQHIoY8TivSHZ3sC = password456
-- $2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q = password789
INSERT INTO users (name, email, password, gender, birth_date, created_at,updated_at) VALUES
    ('John', 'john123@qmail.com', '$2a$10$vYR4pPQqR/oZcUDZfXrahecEejQHY0kLkDB5s.FctPRMcEMh1PYhG', 'M', '1988-05-01', NOW(),NOW()),
     ('Juna', 'juna123@qmail.com', '$2a$10$vYR4pPQqR/oZcUDZfXrahecEejQHY0kLkDB5s.FctPRMcEMh1PYhG', 'M', '2001-01-24',NOW(),NOW()),
     ('Peter', 'peter789@qmail.com', '$2a$10$vYR4pPQqR/oZcUDZfXrahecEejQHY0kLkDB5s.FctPRMcEMh1PYhG', 'M', '1981-12-25',NOW(),NOW()),
     ('Susan', 'susan321@qmail.com', '$2a$10$vYR4pPQqR/oZcUDZfXrahecEejQHY0kLkDB5s.FctPRMcEMh1PYhG', 'F', '1990-06-02',NOW(),NOW()),
     ('David', 'david654@qmail.com', '$2a$10$vYR4pPQqR/oZcUDZfXrahecEejQHY0kLkDB5s.FctPRMcEMh1PYhG', 'M', '1992-03-11',NOW(),NOW()),
     ('Judy', 'judy987@qmail.com', '$2a$10$vYR4pPQqR/oZcUDZfXrahecEejQHY0kLkDB5s.FctPRMcEMh1PYhG', 'F', '1983-10-19',NOW(),NOW()),
     ('Timothy', 'timothy012@qmail.com', '$2a$10$Vqx3VUuB8gy9NvtKHQARWOOYB2wG4wV2WXy1sdQHIoY8TivSHZ3sC', 'M', '1996-11-30',NOW(),NOW()),
     ('Lisa', 'lisa345@qmail.com', '$2a$10$Vqx3VUuB8gy9NvtKHQARWOOYB2wG4wV2WXy1sdQHIoY8TivSHZ3sC', 'F', '1988-07-20',NOW(),NOW()),
     ('Steve', 'steve678@qmail.com', '$2a$10$Vqx3VUuB8gy9NvtKHQARWOOYB2wG4wV2WXy1sdQHIoY8TivSHZ3sC', 'M', '1977-01-05',NOW(),NOW()),
     ('Emily', 'emily321@qmail.com', '$2a$10$Vqx3VUuB8gy9NvtKHQARWOOYB2wG4wV2WXy1sdQHIoY8TivSHZ3sC', 'F', '1994-09-23',NOW(),NOW()),
     ('Henry', 'henry654@qmail.com', '$2a$10$Vqx3VUuB8gy9NvtKHQARWOOYB2wG4wV2WXy1sdQHIoY8TivSHZ3sC', 'M', '1989-06-14',NOW(),NOW()),
     ('Grace', 'grace987@qmail.com', '$2a$10$Vqx3VUuB8gy9NvtKHQARWOOYB2wG4wV2WXy1sdQHIoY8TivSHZ3sC', 'F', '1982-04-28',NOW(),NOW()),
     ('Mike', 'mike012@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'M', '1998-02-08',NOW(),NOW()),
     ('Sophie', 'sophie345@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'F', '1991-12-12',NOW(),NOW()),
     ('Daniel', 'daniel678@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'M', '1980-07-01',NOW(),NOW()),
     ('Olivia', 'olivia321@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'F', '1992-05-28',NOW(),NOW()),
     ('Jackson', 'jackson654@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'M', '1985-02-18',NOW(),NOW()),
     ('Amelia', 'amelia987@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'F', '1995-01-10',NOW(),NOW()),
     ('Tom', 'tom012@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'M', '1987-08-03',NOW(),NOW()),
     ('Sarah', 'sarah345@qmail.com', '$2a$10$ke3IM6noeWfQtX6POjZHl.49gSolYbqfrSTIn8sOQubdwjP2IT94q', 'F', '1984-03-09',NOW(),NOW());
