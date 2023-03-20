SET AUTOCOMMIT = 0;

-- Team
INSERT INTO team(team_id, team_name, number_of_member)
VALUES (1, '1c487536-08ef-4332-bc2f-16830f49495f', 5);

-- Image
INSERT INTO image(image_id, team_id, url)
VALUES (1, 1, 'https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_1.JPG');

INSERT INTO image(image_id, team_id, url)
VALUES (2, 1, 'https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_2.JPG');

INSERT INTO image(image_id, team_id, url)
VALUES (3, 1, 'https://mogong.s3.ap-northeast-2.amazonaws.com/image/sample_3.JPG');

SET AUTOCOMMIT = 1;
