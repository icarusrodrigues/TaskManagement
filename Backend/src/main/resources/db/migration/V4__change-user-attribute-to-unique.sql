ALTER TABLE IF EXISTS public.users
    ADD CONSTRAINT unique_user_email UNIQUE (email);