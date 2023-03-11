DO $$
BEGIN

    --------------------------------------------------------------------------------------------------------------------
    -- UTILITIES
    --------------------------------------------------------------------------------------------------------------------
    CREATE SCHEMA IF NOT EXISTS utilities AUTHORIZATION redhood;

    -- | checks if the schema name given in parameter exists
    -- | param: param_schema_name text
    -- | return: boolean
    CREATE OR REPLACE FUNCTION utilities.schema_exist(
        param_schema_name text)
    RETURNS boolean AS
    $schema_exist$
    DECLARE
        BEGIN
            IF EXISTS (SELECT * FROM pg_catalog.pg_namespace WHERE nspname = param_schema_name) THEN
                RETURN TRUE;
            END IF;
            RETURN FALSE;
        END;
    $schema_exist$
    LANGUAGE plpgsql;
    --------------------------------------------------------------------------------------------------------------------

    -- | checks if the table name given in parameter exists
    -- | params: param_table_name text
    -- |         param_schema_name text
    -- | return: boolean
    CREATE OR REPLACE FUNCTION utilities.table_exist(
        param_table_name text,
        param_schema_name text)
    RETURNS boolean AS
    $table_exist$
    DECLARE
        var_result boolean := false;
        BEGIN
            SELECT true INTO var_result FROM pg_tables WHERE tablename = param_table_name AND schemaname = param_schema_name;
            RETURN coalesce(var_result, false);
        END;
    $table_exist$
    LANGUAGE plpgsql;
    --------------------------------------------------------------------------------------------------------------------

    -- | checks if the column name given in parameter exists
    -- | params: param_table_name text
    -- |         param_column_name text
    -- | return: boolean
    CREATE OR REPLACE FUNCTION utilities.column_exist(
        param_table_name text,
        param_column_name text)
        RETURNS boolean AS
    $column_exist$
    DECLARE
        var_index       integer;
        var_schema      text;
        var_table       text;
        var_sqltable    text;
        var_sqlcolumn   text;
        var_counter     integer := 0;
        BEGIN
            var_schema := null;
            var_table := null;
            SELECT position('.' in param_table_name) INTO var_index;
            IF var_index > 0 THEN
                SELECT substring(param_table_name for(var_index-1)) INTO var_schema;
                SELECT substring(param_table_name from(var_index+1)) INTO var_table;
            ELSE
                var_table := param_table_name;
            END IF;

            var_sqltable := E'
		    	SELECT 	count(*)
		    	FROM 	INFORMATION_SCHEMA.tables t
		    	WHERE	t.table_name = \'' || var_table || E'\'';
            IF var_schema IS NOT NULL THEN
                var_sqltable := var_sqltable || E' AND t.table_schema = \'' || var_schema || E'\'';
            END IF;

            var_sqlcolumn := E'
		    	SELECT 	count(*)
		    	FROM 	INFORMATION_SCHEMA.columns c
		    	WHERE	c.table_name = \'' || var_table || E'\'
		    		AND c.column_name = \'' || param_column_name || E'\'';
            IF var_schema IS NOT NULL THEN
                var_sqlcolumn := var_sqlcolumn || E' AND c.table_schema = \'' || var_schema || E'\'';
            END IF;

            EXECUTE var_sqltable INTO var_counter;
            IF var_counter > 0 THEN
                EXECUTE var_sqlcolumn INTO var_counter;
                IF var_counter > 0 THEN
                    RAISE INFO 'In the table % colum % exist.', param_table_name, param_column_name;
                    RETURN TRUE;
                END IF;
            ELSE
                RAISE EXCEPTION 'Table % - not found.', param_table_name;
            END IF;
            RAISE INFO 'In the table % column % - not found', param_table_name, param_column_name;
            RETURN FALSE;
        END;
    $column_exist$
    LANGUAGE plpgsql;
    --------------------------------------------------------------------------------------------------------------------

    -- | checks if the column name given in parameter exists
    -- | params: param_table_name text
    -- |         param_column_name text
    -- |         param_column_type text
    -- | return: boolean
    CREATE OR REPLACE FUNCTION utilities.column_exist(
        param_table_name text,
        param_column_name text,
        param_column_type text)
        RETURNS boolean AS
    $column_exist$
    DECLARE
        var_index       integer;
        var_schema      text;
        var_table       text;
        var_sqltable    text;
        var_sqlcolumn 	text;
        var_counter 	integer := 0;
    BEGIN
        var_schema := null;
        var_table := null;
        SELECT position('.' in param_table_name) INTO var_index;
        IF var_index > 0 THEN
            SELECT substring(param_table_name for (var_index-1)) INTO var_schema;
            SELECT substring(param_table_name from (var_index+1)) INTO var_table;
        ELSE
            var_table := param_table_name;
        END IF;

        var_sqltable := E'
			SELECT 	count(*)
			FROM 	INFORMATION_SCHEMA.tables t
			WHERE	t.table_name = \'' || param_table_name || E'\'';
        IF var_schema IS NOT NULL THEN
            var_sqltable := var_sqltable || E' AND t.table_schema = \'' || var_schema || E'\'';
        END IF;

        var_sqlcolumn := E'
			SELECT 	count(*)
			FROM 	INFORMATION_SCHEMA.columns c
			WHERE	c.table_name = \'' || param_table_name || E'\'
				AND c.column_name = \'' || param_column_name || E'\'
				AND c.data_type = \'' || param_column_type || E'\'';
        IF var_schema IS NOT NULL THEN
            var_sqlcolumn := var_sqlcolumn || E' AND c.table_schema = \'' || var_schema || E'\'';
        END IF;

        EXECUTE var_sqltable INTO var_counter;
        IF var_counter > 0 THEN
            EXECUTE var_sqlcolumn INTO var_counter;
            IF var_counter > 0 THEN
                RAISE INFO 'In the table % colum % exist.', param_table_name, param_column_name;
                RETURN TRUE;
            END IF;
        ELSE
            RAISE EXCEPTION 'Table % - not found.', param_table_name;
        END IF;

        RAISE INFO 'In the table % column % - not found.', param_table_name, param_column_name;
        RETURN FALSE;
    END;
    $column_exist$
    LANGUAGE plpgsql;
    --------------------------------------------------------------------------------------------------------------------

    -- | function adds columns to existed table
    -- | params: param_table_name text
    -- |         param_column_name text
    -- |         param_column_type text
    CREATE OR REPLACE FUNCTION utilities.add_column(
        param_table_name text,
        param_column_name text,
        param_column_type text)
    RETURNS void AS
    $add_column$
    BEGIN
       EXECUTE utilities.add_column(param_table_name, param_column_name, param_column_type, NULL);
       RETURN;
    END;
    $add_column$
    LANGUAGE plpgsql;
    --------------------------------------------------------------------------------------------------------------------

    -- | function adds columns to existed table
    -- | params: param_table_name text
    -- |         param_column_name text
    -- |         param_column_type text
    -- |         param_comment text
    CREATE OR REPLACE FUNCTION utilities.add_column(
        param_table_name text,
        param_column_name text,
        param_column_type text,
        param_comment text)
        RETURNS void AS
    $add_column$
    BEGIN
       IF NOT utilities.column_exist(param_table_name, param_column_name) THEN
           EXECUTE 'ALTER TABLE ' || param_table_name || ' ADD COLUMN ' || param_column_name || ' ' || param_column_type;
           RAISE INFO 'In the table % added column %', param_table_name, param_column_name;
       END IF;
       IF param_comment IS NOT NULL THEN
           EXECUTE 'COMMENT ON COLUMN ' || param_table_name || '.' || param_column_name ||  ' IS ' || quote_literal(param_comment);
       END IF;
       RETURN;
    END;
    $add_column$
    LANGUAGE plpgsql;
    --------------------------------------------------------------------------------------------------------------------

    -- | function adds columns to existed table
    -- | params: param_table_name text
    -- |         param_column_name text
    -- |         param_column_type text
    -- |         param_comment text
    -- |         param_foreign_key text
    CREATE OR REPLACE FUNCTION utilities.add_column(
        param_table_name text,
        param_column_name text,
        param_column_type text,
        param_comment text,
        param_foreign_key text)
        RETURNS void AS
    $add_column$
    BEGIN
        IF NOT utilities.column_exist(param_table_name, param_column_name) THEN
            EXECUTE ' ALTER TABLE ' || param_table_name || ' ADD COLUMN ' || param_column_name || ' ' || param_column_type;
            RAISE INFO 'In the table % added column %', param_table_name, param_column_name;

            IF param_foreign_key IS NOT NULL THEN
                EXECUTE 'ALTER TABLE ' || param_table_name || ' ADD FOREIGN KEY (' || param_column_name || ') ' || param_foreign_key;
                RAISE INFO 'Added Foreign Key to column % in table %', param_column_name, param_table_name;
            END IF;
        END IF;
        IF param_comment IS NOT NULL THEN
            EXECUTE 'COMMENT ON COLUMN ' || param_table_name || '.' || param_column_name ||  ' IS ' || quote_literal(param_comment);
        END IF;
        RETURN;
    END;
    $add_column$
    LANGUAGE plpgsql;
    --------------------------------------------------------------------------------------------------------------------
    --------------------------------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------------------------------
    -- ACCOUNT
    --------------------------------------------------------------------------------------------------------------------
    CREATE SCHEMA IF NOT EXISTS account AUTHORIZATION redhood;

    -- | creates 'groups' table
    IF utilities.table_exist('groups', 'account') = false THEN
        CREATE TABLE account.groups(
            id serial,
            CONSTRAINT groups_pkey PRIMARY KEY (id)
        );
    END IF;
    EXECUTE utilities.add_column('account.groups', 'group_name', 'text');
    --------------------------------------------------------------------------------------------------------------------

    -- | creates 'accounts' table, that provide user data
    IF utilities.table_exist('accounts', 'account') = false THEN
        CREATE TABLE account.accounts(
            id serial,
            id_group integer,
            CONSTRAINT accounts_pkey PRIMARY KEY (id),
            CONSTRAINT accounts_id_group_fkey FOREIGN KEY (id_group)
                                      REFERENCES account.groups (id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE
        );
    END IF;
    EXECUTE utilities.add_column('account.accounts', 'login', 'text');
    EXECUTE utilities.add_column('account.accounts', 'email', 'text');
    EXECUTE utilities.add_column('account.accounts', 'active', 'boolean');
    EXECUTE utilities.add_column('account.accounts', 'blocked', 'date');
    EXECUTE utilities.add_column('account.accounts', 'name', 'text');
    EXECUTE utilities.add_column('account.accounts', 'last_name', 'text');
    EXECUTE utilities.add_column('account.accounts', 'password', 'text');
    EXECUTE utilities.add_column('account.accounts', 'activation_key', 'text');
    EXECUTE utilities.add_column('account.accounts', 'reset_key', 'text');
    EXECUTE utilities.add_column('account.accounts', 'reset_date', 'date');
    EXECUTE utilities.add_column('account.accounts', 'created_by', 'text');
    EXECUTE utilities.add_column('account.accounts', 'created_date', 'date');
    EXECUTE utilities.add_column('account.accounts', 'last_modified_by', 'text');
    EXECUTE utilities.add_column('account.accounts', 'last_modified_date', 'date');

    --------------------------------------------------------------------------------------------------------------------

    -- | creates 'auth' table, that provide users authorization data
    IF utilities.table_exist('auth', 'account') = false THEN
        CREATE TABLE account.auth(
            id serial,
            id_account integer NOT NULL,
            CONSTRAINT auth_pkey PRIMARY KEY (id),
            CONSTRAINT auth_id_account_fkey FOREIGN KEY (id_account)
                                 REFERENCES account.accounts (id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE
        );
    END IF;
    EXECUTE utilities.add_column('account.auth', 'token', 'TEXT NOT NULL');
    EXECUTE utilities.add_column('account.auth', 'token_time', 'TIMESTAMP WITHOUT TIME ZONE');
    --------------------------------------------------------------------------------------------------------------------

    -- | creates 'permissions' table
    IF utilities.table_exist('permissions', 'account') = false THEN
        CREATE TABLE account.permissions(
            id serial,
            id_group integer,
            CONSTRAINT permissions_pkey PRIMARY KEY (id),
            CONSTRAINT permissions_id_grup_fkey FOREIGN KEY (id_group)
                                        REFERENCES account.groups (id) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE CASCADE
        );
    END IF;
    --
    -- ! TODO: to add permission types
    --
    --------------------------------------------------------------------------------------------------------------------


    --------------------------------------------------------------------------------------------------------------------
    -- PUBLIC/SETTINGS
    --------------------------------------------------------------------------------------------------------------------
    -- | creates 'settings' table
    IF utilities.table_exist('settings', 'public') = false THEN
        CREATE TABLE public.settings(
            id serial,
            CONSTRAINT settings_pkey PRIMARY KEY (id)
        );
    END IF;
    EXECUTE utilities.add_column('public.settings', 'token_validity_time', 'TEXT');
    UPDATE public.settings SET token_validity_time = '602';
    --------------------------------------------------------------------------------------------------------------------
END $$;