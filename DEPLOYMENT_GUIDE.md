# 部署指南

本指南介绍如何将 Todo List 应用部署到生产环境，包含 Docker 部署和传统 Linux + Nginx 部署两种方式。

---

## 一、Docker 部署（推荐）

Docker 部署是最简单的方式，一条命令即可启动完整应用。

### 1.1 环境要求

- Docker 20.10+
- Docker Compose 2.0+

**安装 Docker（Ubuntu）**：

```bash
# 安装 Docker
curl -fsSL https://get.docker.com | sh

# 将当前用户添加到 docker 组
sudo usermod -aG docker $USER

# 重新登录后验证
docker --version
docker compose version
```

### 1.2 快速启动

```bash
# 1. 克隆项目
git clone https://github.com/your-repo/todo-fullstack-demo.git
cd todo-fullstack-demo

# 2. 复制并修改环境变量
cp .env.example .env
# 编辑 .env 文件，设置 MYSQL_ROOT_PASSWORD

# 3. 启动所有服务
docker compose up -d

# 4. 查看服务状态
docker compose ps

# 5. 访问应用
# 前端：http://localhost
# API：http://localhost/api/todos
```

### 1.3 配置说明

**环境变量（.env 文件）**：

| 变量 | 说明 | 默认值 |
|------|------|--------|
| MYSQL_ROOT_PASSWORD | MySQL root 密码 | 必填 |
| MYSQL_DATABASE | 数据库名 | todo_db |
| MYSQL_PORT | MySQL 端口 | 3306 |
| BACKEND_PORT | 后端端口 | 8080 |
| FRONTEND_PORT | 前端端口 | 80 |

### 1.4 常用命令

```bash
# 查看日志
docker compose logs -f              # 所有服务
docker compose logs -f backend      # 仅后端
docker compose logs -f frontend     # 仅前端

# 重启服务
docker compose restart backend

# 停止服务
docker compose down

# 停止并删除数据
docker compose down -v

# 重新构建镜像
docker compose build --no-cache
docker compose up -d
```

### 1.5 数据持久化

MySQL 数据存储在 Docker volume 中：

```bash
# 查看 volume
docker volume ls | grep todo

# 备份数据
docker exec todo-mysql mysqldump -u root -p todo_db > backup.sql

# 恢复数据
docker exec -i todo-mysql mysql -u root -p todo_db < backup.sql
```

---

## 二、传统 Linux + Nginx 部署

### 2.1 Ubuntu 22.04 部署

#### 2.1.1 安装依赖

```bash
# 更新系统
sudo apt update && sudo apt upgrade -y

# 安装 JDK 17
sudo apt install -y openjdk-17-jdk
java -version

# 安装 MySQL 8.0
sudo apt install -y mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql

# 安装 Nginx
sudo apt install -y nginx
sudo systemctl start nginx
sudo systemctl enable nginx

# 安装 Node.js 20 (用于构建前端)
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs
npm install -g pnpm
```

#### 2.1.2 配置 MySQL

```bash
# 安全配置
sudo mysql_secure_installation

# 创建数据库和用户
sudo mysql -u root -p
```

```sql
-- 创建数据库
CREATE DATABASE todo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建专用用户
CREATE USER 'todo_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON todo_db.* TO 'todo_user'@'localhost';
FLUSH PRIVILEGES;

EXIT;
```

#### 2.1.3 部署后端

```bash
# 创建应用目录
sudo mkdir -p /var/www/todo-app/backend
sudo chown -R $USER:$USER /var/www/todo-app

# 构建 JAR 包（在开发机器上）
cd backend
mvn clean package -DskipTests

# 上传 JAR 到服务器
scp target/todo-*.jar user@server:/var/www/todo-app/backend/todo.jar

# 复制 systemd 服务文件
sudo cp deploy/systemd/todo-backend.service /etc/systemd/system/

# 修改服务配置中的密码
sudo nano /etc/systemd/system/todo-backend.service

# 启动服务
sudo systemctl daemon-reload
sudo systemctl enable todo-backend
sudo systemctl start todo-backend

# 检查状态
sudo systemctl status todo-backend
sudo journalctl -u todo-backend -f
```

#### 2.1.4 部署前端

```bash
# 创建前端目录
sudo mkdir -p /var/www/todo-app/frontend

# 构建前端（在开发机器上）
cd frontend
pnpm install
pnpm build

# 上传构建产物到服务器
scp -r dist/* user@server:/var/www/todo-app/frontend/

# 设置权限
sudo chown -R www-data:www-data /var/www/todo-app/frontend
```

#### 2.1.5 配置 Nginx

```bash
# 复制 Nginx 配置
sudo cp deploy/nginx/todo-app.conf /etc/nginx/sites-available/todo-app

# 修改域名
sudo nano /etc/nginx/sites-available/todo-app

# 启用站点
sudo ln -s /etc/nginx/sites-available/todo-app /etc/nginx/sites-enabled/

# 删除默认站点
sudo rm /etc/nginx/sites-enabled/default

# 测试配置
sudo nginx -t

# 重载 Nginx
sudo systemctl reload nginx
```

#### 2.1.6 配置 HTTPS（Let's Encrypt）

```bash
# 安装 Certbot
sudo apt install -y certbot python3-certbot-nginx

# 获取证书（替换为你的域名）
sudo certbot --nginx -d your-domain.com

# 自动续期测试
sudo certbot renew --dry-run
```

证书会自动配置到 Nginx，Certbot 会修改配置文件并设置自动续期。

---

### 2.2 CentOS/RHEL 部署

#### 2.2.1 安装依赖

```bash
# 更新系统
sudo dnf update -y

# 安装 JDK 17
sudo dnf install -y java-17-openjdk java-17-openjdk-devel
java -version

# 安装 MySQL 8.0
sudo dnf install -y mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld

# 安装 Nginx
sudo dnf install -y nginx
sudo systemctl start nginx
sudo systemctl enable nginx

# 安装 Node.js 20
curl -fsSL https://rpm.nodesource.com/setup_20.x | sudo bash -
sudo dnf install -y nodejs
npm install -g pnpm

# 配置防火墙
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --reload
```

#### 2.2.2 配置 MySQL

```bash
# 获取临时密码
sudo grep 'temporary password' /var/log/mysqld.log

# 安全配置
sudo mysql_secure_installation

# 创建数据库和用户（同 Ubuntu）
sudo mysql -u root -p
```

#### 2.2.3 部署后端

步骤与 Ubuntu 相同，仅路径可能不同：

```bash
# SELinux 配置（如果启用）
sudo setsebool -P httpd_can_network_connect 1

# 其余步骤同 Ubuntu 2.1.3
```

#### 2.2.4 部署前端

步骤与 Ubuntu 相同。

#### 2.2.5 配置 Nginx

```bash
# CentOS 的 Nginx 配置目录不同
sudo cp deploy/nginx/todo-app.conf /etc/nginx/conf.d/todo-app.conf

# 测试并重载
sudo nginx -t
sudo systemctl reload nginx
```

#### 2.2.6 配置 HTTPS

```bash
# 安装 Certbot
sudo dnf install -y certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com
```

---

## 三、常见问题排查

### 3.1 Docker 相关

**问题：容器启动失败**

```bash
# 查看详细日志
docker compose logs backend
docker compose logs mysql

# 检查容器状态
docker compose ps -a
```

**问题：MySQL 连接失败**

```bash
# 等待 MySQL 完全启动（healthcheck）
docker compose logs mysql | grep "ready for connections"

# 手动测试连接
docker exec -it todo-mysql mysql -u root -p
```

**问题：前端无法访问 API**

```bash
# 检查 Nginx 配置
docker exec -it todo-frontend cat /etc/nginx/conf.d/nginx.conf

# 测试后端服务
docker exec -it todo-frontend curl http://backend:8080/api/todos
```

### 3.2 传统部署相关

**问题：后端服务无法启动**

```bash
# 查看详细日志
sudo journalctl -u todo-backend -n 100

# 手动运行测试
cd /var/www/todo-app/backend
sudo -u www-data java -jar todo.jar
```

**问题：502 Bad Gateway**

```bash
# 检查后端是否运行
curl http://localhost:8080/api/todos

# 检查 Nginx 错误日志
sudo tail -f /var/log/nginx/todo-app.error.log
```

**问题：权限错误**

```bash
# 检查文件权限
ls -la /var/www/todo-app/

# 修复权限
sudo chown -R www-data:www-data /var/www/todo-app/frontend
sudo chown -R www-data:www-data /var/www/todo-app/backend
```

**问题：SSL 证书错误**

```bash
# 检查证书状态
sudo certbot certificates

# 手动续期
sudo certbot renew

# 检查 Nginx SSL 配置
sudo nginx -t
```

### 3.3 性能优化建议

**JVM 参数优化**：

修改 systemd 服务文件中的 ExecStart：

```ini
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /var/www/todo-app/backend/todo.jar
```

**Nginx 优化**：

```nginx
# 在 nginx.conf 的 http 块中添加
worker_processes auto;
worker_connections 1024;

# 开启 sendfile
sendfile on;
tcp_nopush on;
tcp_nodelay on;
```

**MySQL 优化**：

```ini
# /etc/mysql/mysql.conf.d/mysqld.cnf
innodb_buffer_pool_size = 256M
max_connections = 100
```

---

## 四、更新部署

### 4.1 Docker 更新

```bash
# 拉取最新代码
git pull

# 重新构建并启动
docker compose build --no-cache
docker compose up -d
```

### 4.2 传统部署更新

```bash
# 后端更新
# 1. 在开发机器构建新 JAR
mvn clean package -DskipTests

# 2. 上传到服务器
scp target/todo-*.jar user@server:/var/www/todo-app/backend/todo.jar

# 3. 重启服务
sudo systemctl restart todo-backend

# 前端更新
# 1. 构建
pnpm build

# 2. 上传
scp -r dist/* user@server:/var/www/todo-app/frontend/

# 3. Nginx 不需要重启（静态文件）
```

---

## 五、备份与恢复

### 5.1 数据库备份

```bash
# Docker 环境
docker exec todo-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} todo_db > backup_$(date +%Y%m%d).sql

# 传统环境
mysqldump -u todo_user -p todo_db > backup_$(date +%Y%m%d).sql
```

### 5.2 数据库恢复

```bash
# Docker 环境
docker exec -i todo-mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} todo_db < backup.sql

# 传统环境
mysql -u todo_user -p todo_db < backup.sql
```

---

## 六、监控建议

### 6.1 日志监控

- 后端日志：`/var/log/journal` (systemd) 或 `docker compose logs`
- Nginx 日志：`/var/log/nginx/`
- MySQL 日志：`/var/log/mysql/`

### 6.2 健康检查

```bash
# API 健康检查
curl -f http://localhost:8080/api/todos || echo "Backend down"

# 添加到 crontab 定时检查
*/5 * * * * curl -f http://localhost:8080/api/todos > /dev/null 2>&1 || systemctl restart todo-backend
```

---

祝部署顺利！
