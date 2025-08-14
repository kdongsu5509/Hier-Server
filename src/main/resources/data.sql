-- ------------------------------------------------------------
-- RESET (FK 때문에 자식 → 부모 순서로 삭제)
-- ------------------------------------------------------------
DELETE
FROM comment_images;
DELETE
FROM comment_entity;
DELETE
FROM pin_entity;

-- ------------------------------------------------------------
-- PINS
-- ------------------------------------------------------------
INSERT INTO pin_entity (id, lat, lng, created_at, updated_at, creator_name, name, pin_kakao_map_url)
VALUES
-- 1) 시청 앞 김밥
('11111111-1111-1111-1111-111111111111', 34.881000, 128.622000, TIMESTAMP '2025-08-13 12:00:00',
 TIMESTAMP '2025-08-13 12:00:00', 'admin', '시청앞김밥', 'https://kko.to/geoje-cityhall-kimbap'),
-- 2) 옥포국밥
('22222222-2222-2222-2222-222222222222', 34.889200, 128.686300, TIMESTAMP '2025-08-13 12:01:00',
 TIMESTAMP '2025-08-13 12:01:00', 'admin', '옥포국밥', 'https://kko.to/okpo-gukbap'),
-- 3) 거제바람곱창
('33333333-3333-3333-3333-333333333333', 34.887500, 128.626500, TIMESTAMP '2025-08-13 12:02:00',
 TIMESTAMP '2025-08-13 12:02:00', 'admin', '거제바람곱창', 'https://kko.to/geoje-gopchang'),
-- 4) 장승포 해물라면
('44444444-4444-4444-4444-444444444444', 34.862100, 128.731400, TIMESTAMP '2025-08-13 12:03:00',
 TIMESTAMP '2025-08-13 12:03:00', 'admin', '장승포해물라면', 'https://kko.to/jangseungpo-ramen'),
-- 5) 몽돌해변 회센터
('55555555-5555-5555-5555-555555555555', 34.760800, 128.707900, TIMESTAMP '2025-08-13 12:04:00',
 TIMESTAMP '2025-08-13 12:04:00', 'admin', '몽돌해변 회센터', 'https://kko.to/mongdol-hoe');

-- ------------------------------------------------------------
-- COMMENTS
-- ------------------------------------------------------------
INSERT INTO comment_entity (id, pin_id, creator_name, comment, grade, created_at, updated_at)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '11111111-1111-1111-1111-111111111111', '동수', '김밥 깔끔하고 가성비 좋음.', 4.2,
        TIMESTAMP '2025-08-13 13:00:00', TIMESTAMP '2025-08-13 13:00:00'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '11111111-1111-1111-1111-111111111111', '민수',
        '점심 대기 조금 있음. 그래도 빠르게 회전.', 3.8, TIMESTAMP '2025-08-13 13:10:00', TIMESTAMP '2025-08-13 13:10:00'),

       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3', '22222222-2222-2222-2222-222222222222', '지연', '국밥 진득하고 깔끔한 맛. 반찬도 정갈.',
        4.6, TIMESTAMP '2025-08-13 13:20:00', TIMESTAMP '2025-08-13 13:20:00'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4', '22222222-2222-2222-2222-222222222222', '호준', '양 충분, 가격 합리적.', 4.3,
        TIMESTAMP '2025-08-13 13:30:00', TIMESTAMP '2025-08-13 13:30:00'),

       ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', '33333333-3333-3333-3333-333333333333', '라라', '곱창 신선. 불향 좋고 잡내 없음.',
        4.7, TIMESTAMP '2025-08-13 13:40:00', TIMESTAMP '2025-08-13 13:40:00'),
       ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', '33333333-3333-3333-3333-333333333333', '현수', '예약 추천. 저녁 피크에 혼잡.', 4.0,
        TIMESTAMP '2025-08-13 13:50:00', TIMESTAMP '2025-08-13 13:50:00'),

       ('cccccccc-cccc-cccc-cccc-ccccccccccc1', '44444444-4444-4444-4444-444444444444', '세라', '국물이 시원하고 해물 풍성.', 4.5,
        TIMESTAMP '2025-08-13 14:00:00', TIMESTAMP '2025-08-13 14:00:00'),
       ('cccccccc-cccc-cccc-cccc-ccccccccccc2', '55555555-5555-5555-5555-555555555555', '유리', '회 싱싱, 뷰가 미쳤음.', 4.8,
        TIMESTAMP '2025-08-13 14:10:00', TIMESTAMP '2025-08-13 14:10:00');

-- ------------------------------------------------------------
-- COMMENT IMAGES
-- ------------------------------------------------------------
INSERT INTO comment_images (id, comment_id, image_url)
VALUES ('dddddddd-dddd-dddd-dddd-ddddddddddd1', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
        'https://picsum.photos/id/101/800/600'),
       ('dddddddd-dddd-dddd-dddd-ddddddddddd2', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
        'https://picsum.photos/id/102/800/600'),

       ('dddddddd-dddd-dddd-dddd-ddddddddddd3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3',
        'https://picsum.photos/id/103/800/600'),

       ('dddddddd-dddd-dddd-dddd-ddddddddddd4', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
        'https://picsum.photos/id/104/800/600'),
       ('dddddddd-dddd-dddd-dddd-ddddddddddd5', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
        'https://picsum.photos/id/105/800/600'),

       ('dddddddd-dddd-dddd-dddd-ddddddddddd6', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
        'https://picsum.photos/id/106/800/600'),

       ('dddddddd-dddd-dddd-dddd-ddddddddddd7', 'cccccccc-cccc-cccc-cccc-ccccccccccc1',
        'https://picsum.photos/id/107/800/600'),

       ('dddddddd-dddd-dddd-dddd-ddddddddddd8', 'cccccccc-cccc-cccc-cccc-ccccccccccc2',
        'https://picsum.photos/id/108/800/600'),
       ('dddddddd-dddd-dddd-dddd-ddddddddddd9', 'cccccccc-cccc-cccc-cccc-ccccccccccc2',
        'https://picsum.photos/id/109/800/600');
