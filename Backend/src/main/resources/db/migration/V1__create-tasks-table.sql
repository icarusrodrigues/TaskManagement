CREATE TABLE public.tasks (
    id bigint NOT NULL,
    code text NOT NULL,
    title text NOT NULL,
    status text NOT NULL,
    description text,
    due_date timestamp,
    PRIMARY KEY (id)
);

CREATE SEQUENCE public.task_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

ALTER SEQUENCE public.task_id_seq
    OWNER TO postgres;

ALTER TABLE public.tasks ALTER COLUMN id SET DEFAULT NEXTVAL('task_id_seq'::regclass);