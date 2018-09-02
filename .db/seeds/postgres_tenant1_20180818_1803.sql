--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3 (Debian 10.3-1.pgdg90+1)
-- Dumped by pg_dump version 10.3 (Debian 10.3-1.pgdg90+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.dwh_users DROP CONSTRAINT IF EXISTS table_name_pkey;
DROP TABLE IF EXISTS public.dwh_users;
DROP SCHEMA IF EXISTS public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: dwh_users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dwh_users (
    column_1 integer NOT NULL,
    name character varying
);


--
-- Data for Name: dwh_users; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.dwh_users (column_1, name) FROM stdin;
1	ahoj
\.


--
-- Name: dwh_users table_name_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dwh_users
    ADD CONSTRAINT table_name_pkey PRIMARY KEY (column_1);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: -
--

GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

