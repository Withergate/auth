INSERT INTO users (user_id, email, pending_email, password, role, enabled) VALUES
	('1', 'admin@withergate.com', null, '$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri', 'ADMIN', true);

INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope,
authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity,
refresh_token_validity, additional_information, autoapprove) VALUES
	('withergate', 'resource', '$2a$10$s7rR9qxaUnOJaE3J6ZSICupQxm.xTJhvScmXV.ylsI3AIY5OMXp.q', 'read,write', 'authorization_code,refresh_token,implicit,password,client_credentials', 'https://game.withergate.com', '', 3600, 2592000, '{}', 'true');
