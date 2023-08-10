# shopping-mall-system-backend
이커머스 관련 프로젝트 샘플(백엔드)


필수 테이블 스크립트


CREATE TABLE `SYS_USER` (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '아이디',
`login_id` varchar(1000) NOT NULL COMMENT '로그인_아이디',
`login_pass` varchar(1000) NOT NULL COMMENT '로그인_패스워드',
`user_nm` varchar(100) NOT NULL COMMENT '사용자_명',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- automationDB.API_AUTH_TOKEN definition

CREATE TABLE `API_AUTH_TOKEN` (
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '아이디',
`user_id` int(11) NOT NULL COMMENT '사용자_아이디',
`acc_token` varchar(1000) NOT NULL COMMENT 'Access Token',
`acc_token_expire` datetime NOT NULL COMMENT 'Access Token 만료일시',
`ref_token` varchar(1000) NOT NULL COMMENT 'Refresh Token',
`ref_token_expire` datetime NOT NULL COMMENT 'Refresh Token 만료일시',
`cre_tm` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일시',
`cre_user` varchar(1000) NOT NULL DEFAULT 'SYSTEM' COMMENT '등록자',
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;