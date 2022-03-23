DROP database if exists `search`;
create database `search`;

use `search`;

# boss直聘 职位表
DROP TABLE IF EXISTS `boss_job`;
CREATE TABLE `boss_job` (
    `id` int(11) unsigned not null AUTO_INCREMENT comment 'id',
    `name` varchar(255) not null comment '职位名称',
    `area` varchar(255) comment '工作地域',
    `salary` varchar(100) comment '薪资范围',
    `agelimit` varchar(100) comment '年限',
    `education` varchar(100) comment '学历',
    `recruiter` varchar(100) comment '招聘人',
    `recruiter_position` varchar(255) comment '招聘人职位',
    `company_name` varchar(255) comment '公司名称',
    `company_type` varchar(255) comment '公司分类',
    `company_logo` varchar(255) comment '公司logo',
    `finance` varchar(100) comment '融资情况',
    `company_size` varchar(100) comment '规模',
    `tags` varchar(500) comment '技术标签',
    `welfare` varchar(255) comment '福利',
    `create_id` int(11) comment '创建人id',
    `create_date` datetime comment '创建时间',
    `update_id` int(11) comment '更新人id',
    `update_date` datetime comment '更新时间',
    `remark` varchar(255) comment '备注',
    primary key (`id`)
) engine=InnoDB DEFAULT CHARSET=utf8 comment='boss职位表';