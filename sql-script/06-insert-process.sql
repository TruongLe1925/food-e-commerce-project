INSERT INTO `e-commerce`.status (id, name, description) VALUES 
(1, 'PENDING', 'Đơn hàng mới đã được đặt thành công và đang chờ hệ thống xác nhận.'),
(2, 'CONFIRMED', 'Đơn hàng đã được kiểm tra và xác nhận hợp lệ.'),
(3, 'PROCESSING', 'Cửa hàng đang tiến hành chuẩn bị sản phẩm hoặc đóng gói hàng hóa.'),
(4, 'COMPLETED', 'Đơn hàng đã được giao tận tay khách hàng thành công.'),
(5, 'CANCELLED', 'Đơn hàng đã bị hủy bỏ bởi người dùng hoặc hệ thống.');
INSERT INTO `e-commerce`.promotion (name, discount_type, discount_value, start_date, end_date) 
VALUES 
-- Khuyến mãi theo phần trăm (Ví dụ: Giảm 20%)
('Chương trình Hè Rực Rỡ', 'PERCENTAGE', 20.00, '2026-06-01 00:00:00', '2026-08-31 23:59:59'),

-- Khuyến mãi theo số tiền cố định (Ví dụ: Giảm 50.000 VNĐ)
('Ưu đãi Khách hàng Mới', 'FIXED_AMOUNT', 50000.00, '2026-01-01 00:00:00', '2026-12-31 23:59:59');