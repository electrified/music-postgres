CREATE TABLE artist (
    id uuid PRIMARY KEY,
    name CHARACTER VARYING(255) NOT NULL,
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE source (
    id uuid PRIMARY KEY,
    path CHARACTER VARYING(255) NOT NULL,
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE audio_file (
    id uuid PRIMARY KEY,
    relative_path CHARACTER VARYING(4096) NOT NULL,
    md5sum CHARACTER VARYING(255),
    filetype CHARACTER VARYING(255),
    bitrate integer,
    vbr boolean,
    samplerate integer,
    has_Id3v1Tag boolean,
    has_Id3v2Tag boolean,
    source_id uuid NOT NULL REFERENCES source(id),
    track_id uuid,
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE release (
    id uuid PRIMARY KEY,
    name CHARACTER VARYING(255),
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE track (
    id uuid PRIMARY KEY,
    name CHARACTER VARYING(255),
    track CHARACTER VARYING(8),
    year integer,
    genre CHARACTER VARYING(255),
    comment CHARACTER VARYING(255),
    length integer NOT NULL,
    created_date timestamp with time zone NOT NULL
);

CREATE TABLE track_artist (
    track_id UUID NOT NULL,
    artist_id UUID NOT NULL REFERENCES artist(id)
);

CREATE TABLE track_release (
    track_id UUID NOT NULL,
    release_id UUID NOT NULL REFERENCES release(id)
);

CREATE TABLE track_audiofile (
    track_id UUID NOT NULL,
    audiofile_id UUID NOT NULL REFERENCES audio_file(id)
);
