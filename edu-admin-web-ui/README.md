# edu-admin-web-ui

智学教育管理平台前端，与后端工程 **edu-admin-web**（`com.eduadmin`）配套使用。

## 技术栈

- Vue 3 + Vite
- Vue Router / Pinia
- Element Plus + Axios

## 开发

```bash
npm install
npm run dev
```

默认通过 Vite 代理将 `/api` 转发到 `http://localhost:8080`（后端 `EduAdminApplication`）。

## 构建

```bash
npm run build
```

产物输出到 `dist/`，可部署到 Nginx 等静态服务器。

## 项目对应关系

| 层级 | 名称 |
|------|------|
| 后端 Maven 工程 | `edu-admin-web` |
| 后端 Java 包 | `com.eduadmin` |
| 前端 npm 包 | `edu-admin-web-ui` |
| 数据库 | `edu_admin` |

## 演示登录

导入 `sql/02_sample_data.sql` 后，可使用 `admin` / `123456` 登录。
