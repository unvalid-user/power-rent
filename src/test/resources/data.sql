SET REFERENTIAL_INTEGRITY FALSE;

DELETE FROM rentals;
DELETE FROM stations;
DELETE FROM powerbanks;
DELETE FROM users;
DELETE FROM station_commands;

DELETE FROM dispense_commands;
DELETE FROM receive_commands;
DELETE FROM sync_commands;


ALTER TABLE rentals ALTER COLUMN id RESTART WITH 1;
ALTER TABLE stations ALTER COLUMN id RESTART WITH 1;
ALTER TABLE powerbanks ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE station_commands ALTER COLUMN id RESTART WITH 1;

ALTER TABLE dispense_commands ALTER COLUMN id RESTART WITH 1;
ALTER TABLE receive_commands ALTER COLUMN id RESTART WITH 1;
ALTER TABLE sync_commands ALTER COLUMN id RESTART WITH 1;


INSERT INTO users (id, email, username, password, role)
VALUES (1, 'customer@email.com', 'test-customer', '$2a$10$WzlqvNg3rEdIiSKU2fOmeeyzTiHzT98FjW15ujTirGfnlutsJU4d.', 'CUSTOMER'),
       (2, 'admin@email.com', 'test-admin', '$2a$10$4iU3sQSKRNo55xulDw6X/eNzczM8HWIhJ1RlltpLoJiwdFdq8VP76', 'ADMIN');


INSERT INTO stations (id, code, name, location, powerbank_type, total_slots, rent_price)
VALUES (1, 'ST001', 'station1', 'address1', 'COMMON', 8, 200),
       (2, 'ST002', 'station2', 'address2', 'COMMON', 8, 170),
       (3, 'ST103', 'station3', 'address3', 'ADVANCED', 6, 300);

INSERT INTO powerbanks (id, serial_number, type, status, station_id)
VALUES (1, 'pb234958u23', 'COMMON', 'CHARGING', 1),
       (2, 'pb3456wg434', 'COMMON', 'AVAILABLE', 1),
       (3, 'pb345ge3459', 'COMMON', 'AVAILABLE', 1),
       (4, 'pb6j329fk02', 'ADVANCED', 'CHARGING', 2),
       (5, 'pb23jh2340l', 'ADVANCED', 'AVAILABLE', 2);


INSERT INTO rentals (id, user_id, start_station_id, start_time, rent_price, status)
VALUES (100, 2, 1, '2024-01-07 12:00:00', 100, 'CANCELED'),
       (101, 2, 1, '2024-02-07 12:00:00', 100, 'COMPLETED'),
       (102, 2, 1, '2024-03-07 12:00:00', 100, 'COMPLETED'),
       (103, 2, 1, '2024-04-07 12:00:00', 100, 'COMPLETED'),
       (104, 2, 1, '2024-05-07 12:00:00', 100, 'COMPLETED'),
       (105, 2, 2, '2024-06-07 12:00:00', 100, 'CANCELED'),
       (106, 2, 2, '2024-07-07 12:00:00', 100, 'COMPLETED'),
       (107, 2, 2, '2024-08-07 12:00:00', 100, 'COMPLETED'),
       (108, 2, 2, '2024-09-07 12:00:00', 100, 'COMPLETED'),
       (109, 2, 2, '2024-10-07 12:00:00', 100, 'COMPLETED'),
       (110, 2, 3, '2024-11-07 12:00:00', 100, 'CANCELED'),
       (111, 2, 3, '2024-12-07 12:00:00', 100, 'COMPLETED'),
       (112, 2, 3, '2025-01-07 12:00:00', 100, 'COMPLETED'),
       (113, 2, 3, '2025-02-07 12:00:00', 100, 'COMPLETED'),
       (114, 2, 3, '2025-03-07 12:00:00', 100, 'COMPLETED');

