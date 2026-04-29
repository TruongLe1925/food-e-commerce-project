CREATE DATABASE IF NOT EXISTS admissionPredicDB;
USE admissionPredicDB;

DROP TABLE IF EXISTS university;

CREATE TABLE university (
    id INT AUTO_INCREMENT PRIMARY KEY,
    university_name VARCHAR(255) NOT NULL,
    average_admission_score DECIMAL(6, 2), -- Đổi tên để phản ánh đúng ý nghĩa
    year INT NOT NULL
);

INSERT INTO university (university_name, average_admission_score, year) 
VALUES 
('Đại học Bách Khoa', 27.63, 2023),
('Đại học Kinh tế Quốc dân', 27.88, 2023),
('Đại học Ngoại thương', 28.90, 2023),
('Đại học Công nghệ - ĐHQGHN', 27.85, 2024);

USE admissionPredicDB;

-- Xóa dữ liệu cũ nếu bạn muốn làm sạch bảng trước khi chèn
TRUNCATE TABLE university;

INSERT INTO university (university_name, average_admission_score, year) 
VALUES 
-- Khối Kỹ thuật & Công nghệ
('Đại học Bách Khoa Hà Nội', 28.15, 2025),
('Đại học Bách Khoa - ĐHQG TP.HCM', 27.90, 2025),
('Đại học Công nghệ - ĐHQGHN', 27.85, 2025),
('Đại học Công nghệ thông tin - ĐHQG TP.HCM', 28.20, 2025),
('Đại học Sư phạm Kỹ thuật TP.HCM', 26.50, 2025),
('Đại học Sư phạm Kỹ thuật Nam Định', 22.30, 2025),
('Đại học Công nghiệp Hà Nội', 25.80, 2025),
('Đại học Công nghiệp TP.HCM', 25.40, 2025),
('Đại học Giao thông Vận tải', 25.60, 2025),
('Đại học Thủy lợi', 24.75, 2025),
('Đại học Xây dựng Hà Nội', 24.20, 2025),
('Đại học Điện lực', 23.90, 2025),
('Đại học Mỏ - Địa chất', 22.50, 2025),
('Đại học FPT', 25.00, 2025),

-- Khối Kinh tế & Luật
('Đại học Ngoại thương (FTU)', 28.95, 2025),
('Đại học Kinh tế Quốc dân (NEU)', 28.25, 2025),
('Đại học Kinh tế TP.HCM (UEH)', 27.60, 2025),
('Đại học Ngân hàng TP.HCM', 26.40, 2025),
('Đại học Tài chính - Marketing', 25.85, 2025),
('Đại học Thương mại', 26.90, 2025),
('Đại học Luật Hà Nội', 27.15, 2025),
('Đại học Luật TP.HCM', 26.80, 2025),
('Đại học Kinh tế - Luật (UEL)', 27.45, 2025),
('Đại học Quản lý và Công nghệ TP.HCM', 21.50, 2025),

-- Khối Y Dược & Khoa học tự nhiên
('Đại học Y Hà Nội', 28.70, 2025),
('Đại học Y Dược TP.HCM', 28.40, 2025),
('Đại học Y Dược Cần Thơ', 26.95, 2025),
('Đại học Y Khoa Phạm Ngọc Thạch', 27.20, 2025),
('Đại học Dược Hà Nội', 27.80, 2025),
('Đại học Khoa học Tự nhiên - ĐHQGHN', 26.10, 2025),
('Đại học Khoa học Tự nhiên - ĐHQG TP.HCM', 25.90, 2025),

-- Khối Ngôn ngữ & Sư phạm & Nhân văn
('Đại học Ngoại ngữ - ĐHQGHN', 27.50, 2025),
('Đại học Hà Nội (HANU)', 27.30, 2025),
('Đại học Sư phạm Hà Nội', 27.90, 2025),
('Đại học Sư phạm TP.HCM', 27.65, 2025),
('Đại học Khoa học Xã hội và Nhân văn - TP.HCM', 26.20, 2025),
('Đại học Khoa học Xã hội và Nhân văn - Hà Nội', 26.85, 2025),
('Đại học Sài Gòn', 26.10, 2025),

-- Khối Tổng hợp & Địa phương
('Đại học Cần Thơ', 25.30, 2025),
('Đại học Đà Nẵng - Bách khoa', 26.15, 2025),
('Đại học Đà Nẵng - Kinh tế', 25.80, 2025),
('Đại học Vinh', 22.40, 2025),
('Đại học Quy Nhơn', 21.80, 2025),
('Đại học Thái Nguyên', 21.00, 2025),
('Đại học Tôn Đức Thắng', 27.05, 2025),
('Đại học Mở Hà Nội', 25.20, 2025),
('Đại học Mở TP.HCM', 24.90, 2025),
('Đại học Văn Lang', 20.00, 2025),
('Đại học Hoa Sen', 19.50, 2025);

-- 1. Tạo bảng major
CREATE TABLE IF NOT EXISTS major (
    major_id INT AUTO_INCREMENT PRIMARY KEY,
    major_name VARCHAR(255) NOT NULL,
    university_id INT,
    FOREIGN KEY (university_id) REFERENCES university(id) ON DELETE CASCADE
);
-- Thêm Ngành học cho các trường (Ví dụ dựa trên ID của trường)
INSERT INTO major (major_name, university_id) VALUES
-- Đại học Bách Khoa Hà Nội (id: 1)
('Khoa học Máy tính', 1),
('Kỹ thuật Cơ điện tử', 1),

-- Đại học Bách Khoa - ĐHQG TP.HCM (id: 2)
('Khoa học Máy tính', 2),
('Kỹ thuật Ô tô', 2),

-- Đại học Công nghệ thông tin - ĐHQG TP.HCM (id: 4)
('An toàn thông tin', 4),
('Kỹ thuật Phần mềm', 4),

-- Đại học Ngoại thương (id: 15)
('Kinh tế đối ngoại', 15),
('Quản trị kinh doanh', 15),

-- Đại học Kinh tế Quốc dân (id: 16)
('Logistics và Quản lý chuỗi cung ứng', 16),
('Tài chính - Ngân hàng', 16),

-- Đại học Y Hà Nội (id: 25)
('Y Đa khoa', 25),
('Răng Hàm Mặt', 25),

-- Đại học FPT (id: 14)
('Kỹ thuật Phần mềm', 14),
('Thiết kế đồ họa', 14);
-- Khối Kỹ thuật & Công nghệ (Tiếp theo)
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ kỹ thuật cơ điện tử', id FROM university WHERE university_name LIKE '%Sư phạm Kỹ thuật TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Sư phạm tiếng Anh', id FROM university WHERE university_name LIKE '%Sư phạm Kỹ thuật TP.HCM%';

INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ kỹ thuật xây dựng', id FROM university WHERE university_name LIKE '%Giao thông Vận tải%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật điều khiển và tự động hóa', id FROM university WHERE university_name LIKE '%Giao thông Vận tải%';
INSERT INTO major (major_name, university_id) 
SELECT 'Logistics và quản lý chuỗi cung ứng', id FROM university WHERE university_name LIKE '%Giao thông Vận tải%';

INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ thông tin', id FROM university WHERE university_name LIKE '%Thủy lợi%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật tài nguyên nước', id FROM university WHERE university_name LIKE '%Thủy lợi%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kế toán', id FROM university WHERE university_name LIKE '%Thủy lợi%';

INSERT INTO major (major_name, university_id) 
SELECT 'Kiến trúc', id FROM university WHERE university_name LIKE '%Xây dựng Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật xây dựng', id FROM university WHERE university_name LIKE '%Xây dựng Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ thông tin', id FROM university WHERE university_name LIKE '%Xây dựng Hà Nội%';

INSERT INTO major (major_name, university_id) 
SELECT 'Hệ thống điện', id FROM university WHERE university_name LIKE '%Điện lực%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản lý năng lượng', id FROM university WHERE university_name LIKE '%Điện lực%';

-- Khối Kinh tế & Luật (Tiếp theo)
INSERT INTO major (major_name, university_id) 
SELECT 'Tài chính - Ngân hàng', id FROM university WHERE university_name LIKE '%Ngân hàng TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị kinh doanh', id FROM university WHERE university_name LIKE '%Ngân hàng TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Luật kinh tế', id FROM university WHERE university_name LIKE '%Ngân hàng TP.HCM%';

INSERT INTO major (major_name, university_id) 
SELECT 'Kinh doanh thương mại', id FROM university WHERE university_name LIKE '%Thương mại%';
INSERT INTO major (major_name, university_id) 
SELECT 'Thương mại điện tử', id FROM university WHERE university_name LIKE '%Thương mại%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị khách sạn', id FROM university WHERE university_name LIKE '%Thương mại%';

INSERT INTO major (major_name, university_id) 
SELECT 'Luật hình sự', id FROM university WHERE university_name LIKE '%Luật Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Luật thương mại quốc tế', id FROM university WHERE university_name LIKE '%Luật Hà Nội%';

INSERT INTO major (major_name, university_id) 
SELECT 'Kinh tế đối ngoại', id FROM university WHERE university_name LIKE '%Kinh tế - Luật (UEL)%';
INSERT INTO major (major_name, university_id) 
SELECT 'Thương mại điện tử', id FROM university WHERE university_name LIKE '%Kinh tế - Luật (UEL)%';

-- Khối Y Dược & Tự nhiên (Tiếp theo)
INSERT INTO major (major_name, university_id) 
SELECT 'Y khoa', id FROM university WHERE university_name LIKE '%Y Khoa Phạm Ngọc Thạch%';
INSERT INTO major (major_name, university_id) 
SELECT 'Y học cổ truyền', id FROM university WHERE university_name LIKE '%Y Khoa Phạm Ngọc Thạch%';

INSERT INTO major (major_name, university_id) 
SELECT 'Hóa dược', id FROM university WHERE university_name LIKE '%Dược Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Dược lâm sàng', id FROM university WHERE university_name LIKE '%Dược Hà Nội%';

INSERT INTO major (major_name, university_id) 
SELECT 'Sinh học', id FROM university WHERE university_name LIKE '%Khoa học Tự nhiên - ĐHQG TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Hóa học', id FROM university WHERE university_name LIKE '%Khoa học Tự nhiên - ĐHQG TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ sinh học', id FROM university WHERE university_name LIKE '%Khoa học Tự nhiên - ĐHQG TP.HCM%';

-- Khối Tổng hợp & Địa phương (Tiếp theo)
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật cơ khí', id FROM university WHERE university_name LIKE '%Đà Nẵng - Bách khoa%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật điện tử - Viễn thông', id FROM university WHERE university_name LIKE '%Đà Nẵng - Bách khoa%';

INSERT INTO major (major_name, university_id) 
SELECT 'Marketing', id FROM university WHERE university_name LIKE '%Đà Nẵng - Kinh tế%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị dịch vụ du lịch và lữ hành', id FROM university WHERE university_name LIKE '%Đà Nẵng - Kinh tế%';

INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ thông tin', id FROM university WHERE university_name LIKE '%Cần Thơ%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật phần mềm', id FROM university WHERE university_name LIKE '%Cần Thơ%';
INSERT INTO major (major_name, university_id) 
SELECT 'Nông nghiệp', id FROM university WHERE university_name LIKE '%Cần Thơ%';
INSERT INTO major (major_name, university_id) 
SELECT 'Nuôi trồng thủy sản', id FROM university WHERE university_name LIKE '%Cần Thơ%';

INSERT INTO major (major_name, university_id) 
SELECT 'Ngôn ngữ Anh', id FROM university WHERE university_name LIKE '%Văn Lang%';
INSERT INTO major (major_name, university_id) 
SELECT 'Thiết kế thời trang', id FROM university WHERE university_name LIKE '%Văn Lang%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quan hệ công chúng', id FROM university WHERE university_name LIKE '%Văn Lang%';

INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị khách sạn', id FROM university WHERE university_name LIKE '%Hoa Sen%';
INSERT INTO major (major_name, university_id) 
SELECT 'Tâm lý học', id FROM university WHERE university_name LIKE '%Hoa Sen%';








-- TIẾP TỤC BỔ SUNG DỮ LIỆU BẢNG MAJOR

-- Khối Kỹ thuật & Công nghệ (Bổ sung sâu)
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật Robot và Trí tuệ nhân tạo', id FROM university WHERE university_name LIKE '%Bách Khoa Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật Hàng không', id FROM university WHERE university_name LIKE '%Bách Khoa - ĐHQG TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ Kỹ thuật Cơ điện tử', id FROM university WHERE university_name LIKE '%Công nghiệp TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ Kỹ thuật Hóa học', id FROM university WHERE university_name LIKE '%Công nghiệp TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật Mỏ', id FROM university WHERE university_name LIKE '%Mỏ - Địa chất%';
INSERT INTO major (major_name, university_id) 
SELECT 'Địa chất học', id FROM university WHERE university_name LIKE '%Mỏ - Địa chất%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật Điện tử - Viễn thông', id FROM university WHERE university_name LIKE '%Sư phạm Kỹ thuật Nam Định%';

-- Khối Kinh tế & Luật (Bổ sung sâu)
INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị Tài chính quốc tế', id FROM university WHERE university_name LIKE '%Ngoại thương (FTU)%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kinh doanh quốc tế', id FROM university WHERE university_name LIKE '%Tài chính - Marketing%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị thương hiệu', id FROM university WHERE university_name LIKE '%Tài chính - Marketing%';
INSERT INTO major (major_name, university_id) 
SELECT 'Bất động sản', id FROM university WHERE university_name LIKE '%Kinh tế Quốc dân (NEU)%';
INSERT INTO major (major_name, university_id) 
SELECT 'Luật dân sự', id FROM university WHERE university_name LIKE '%Luật TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị chuỗi cung ứng', id FROM university WHERE university_name LIKE '%Quản lý và Công nghệ TP.HCM%';

-- Khối Y Dược & Tự nhiên (Bổ sung sâu)
INSERT INTO major (major_name, university_id) 
SELECT 'Y học dự phòng', id FROM university WHERE university_name LIKE '%Y Dược Cần Thơ%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật xét nghiệm y học', id FROM university WHERE university_name LIKE '%Y Dược Cần Thơ%';
INSERT INTO major (major_name, university_id) 
SELECT 'Khoa học vật liệu', id FROM university WHERE university_name LIKE '%Khoa học Tự nhiên - ĐHQGHN%';
INSERT INTO major (major_name, university_id) 
SELECT 'Khí tượng và khí hậu học', id FROM university WHERE university_name LIKE '%Khoa học Tự nhiên - ĐHQGHN%';

-- Khối Ngôn ngữ & Sư phạm & Nhân văn (Bổ sung sâu)
INSERT INTO major (major_name, university_id) 
SELECT 'Sư phạm Ngữ văn', id FROM university WHERE university_name LIKE '%Sư phạm Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Sư phạm Tiếng Anh', id FROM university WHERE university_name LIKE '%Sư phạm TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Văn hóa học', id FROM university WHERE university_name LIKE '%Khoa học Xã hội và Nhân văn - Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản trị dịch vụ du lịch và lữ hành', id FROM university WHERE university_name LIKE '%Khoa học Xã hội và Nhân văn - Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Đông phương học', id FROM university WHERE university_name LIKE '%Sài Gòn%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kỹ thuật phần mềm', id FROM university WHERE university_name LIKE '%Sài Gòn%';

-- Khối Tổng hợp & Địa phương (Bổ sung sâu)
INSERT INTO major (major_name, university_id) 
SELECT 'Kinh tế học', id FROM university WHERE university_name LIKE '%Vinh%';
INSERT INTO major (major_name, university_id) 
SELECT 'Sư phạm Mầm non', id FROM university WHERE university_name LIKE '%Vinh%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ thực phẩm', id FROM university WHERE university_name LIKE '%Quy Nhơn%';
INSERT INTO major (major_name, university_id) 
SELECT 'Quản lý đất đai', id FROM university WHERE university_name LIKE '%Thái Nguyên%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ Kỹ thuật Điện tử', id FROM university WHERE university_name LIKE '%Thái Nguyên%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ thông tin', id FROM university WHERE university_name LIKE '%Mở Hà Nội%';
INSERT INTO major (major_name, university_id) 
SELECT 'Công nghệ sinh học', id FROM university WHERE university_name LIKE '%Mở TP.HCM%';
INSERT INTO major (major_name, university_id) 
SELECT 'Kiến trúc nội thất', id FROM university WHERE university_name LIKE '%Tôn Đức Thắng%';