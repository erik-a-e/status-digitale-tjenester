--oh prefix = opening hours

CREATE TABLE oh_rule
(
    id          UUID         NOT NULL,
    name        VARCHAR(100) NOT NULL,
    rule        VARCHAR(100)  NOT NULL,
    created_at  timestamp with time zone  NOT NULL DEFAULT NOW(),
    updated_at  timestamp with time zone NULL,
    PRIMARY KEY (id)
);


CREATE TABLE oh_group
(
    id                      UUID         NOT NULL,
    name                    VARCHAR(100) NOT NULL,
    rule_group_ids          ARRAY      NOT NULL,
    created_at  timestamp with time zone  NOT NULL DEFAULT NOW(),
    updated_at  timestamp with time zone NULL,
    PRIMARY KEY (id)
);

--CREATE TABLE oh_group_rule
--(
--    group_id          UUID         NOT NULL,
--    rule_group_ids           ARRAY      NOT NULL,
--    created_at  timestamp with time zone  NOT NULL DEFAULT NOW(),
--    updated_at  timestamp with time zone NULL,
--    PRIMARY KEY (group_id, rule_id)
----    FOREIGN KEY (rule_id) REFERENCES oh_rule (id),
----    FOREIGN KEY (group_id) REFERENCES oh_group (id)
--);
--
--CREATE TABLE oh_group_group
--(
--    group_id          UUID         NOT NULL,
--    sub_group_id          UUID        NOT NULL,
--    created_at  timestamp with time zone  NOT NULL DEFAULT NOW(),
--    updated_at  timestamp with time zone NULL,
--    PRIMARY KEY (group_id, sub_group_id),
--    FOREIGN KEY (group_id) REFERENCES oh_group (id),
--    FOREIGN KEY (sub_group_id) REFERENCES oh_group (id)
--);