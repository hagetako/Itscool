CREATE TABLE Routes(RouteID integer PRIMARY KEY, Departure text NOT NULL, Arrival text NOT NULL, TimetableURL text NOT NULL);
!
INSERT INTO Routes(RouteID, Departure, Arrival, TimetableURL) VALUES (1, 'JR高槻駅北', '関西大学','http://www.city.takatsuki.osaka.jp/new2001/bus/209_5.html');
!
INSERT INTO Routes(RouteID, Departure, Arrival, TimetableURL) VALUES (2, 'JR富田駅北', '関西大学','http://www.city.takatsuki.osaka.jp/new2001/bus/315_1.html');
!
INSERT INTO Routes(RouteID, Departure, Arrival, TimetableURL) VALUES (3, '関西大学', 'JR高槻駅北', 'http://www.city.takatsuki.osaka.jp/new2001/bus/268_12.html');
!
INSERT INTO Routes(RouteID, Departure, Arrival, TimetableURL) VALUES (4, '関西大学', 'JR富田駅北','http://www.city.takatsuki.osaka.jp/new2001/bus/268_2.html');