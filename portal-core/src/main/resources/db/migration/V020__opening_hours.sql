CREATE TABLE opening_rule
(
    id          UUID         NOT NULL,
    name        VARCHAR(100) NOT NULL,
    rule        VARCHAR(100)  NOT NULL,
    created_at  timestamp with time zone  NOT NULL DEFAULT NOW(),
    updated_at  timestamp with time zone NULL,
    PRIMARY KEY (id)
);


CREATE TABLE opening_rule_group
(
    id          UUID         NOT NULL,
    name        VARCHAR(100) NOT NULL,
    created_at  timestamp with time zone  NOT NULL DEFAULT NOW(),
    updated_at  timestamp with time zone NULL,
    PRIMARY KEY (id)
);

CREATE TABLE opening_hours_connection
(
    group_id          UUID         NOT NULL,
    rule_id          UUID        NOT NULL,
    created_at  timestamp with time zone  NOT NULL DEFAULT NOW(),
    updated_at  timestamp with time zone NULL,
    PRIMARY KEY (group_id, rule_id),
    FOREIGN KEY (rule_id) REFERENCES opening_rule (id),
    FOREIGN KEY (group_id) REFERENCES opening_rule_group (id)
);