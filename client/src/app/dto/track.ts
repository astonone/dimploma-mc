export class Track {
    private _id : number;
    private _title : string;
    private _artist : string;
    private _album : string;
    private _year : string;
    private _filename : string;
    private _duration : string;
    private _rating : number;
    private _tempRating : number;


    constructor(data: any) {
        this._id = data.id;
        this._title = data.title;
        this._artist = data.artist;
        this._album = data.album;
        this._year = data.year;
        this._filename = data.filename;
        this._duration = data.duration;
        this._rating = data.rating;
        this._tempRating = null;
    }


    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get title(): string {
        return this._title;
    }

    set title(value: string) {
        this._title = value;
    }

    get artist(): string {
        return this._artist;
    }

    set artist(value: string) {
        this._artist = value;
    }

    get album(): string {
        return this._album;
    }

    set album(value: string) {
        this._album = value;
    }

    get year(): string {
        return this._year;
    }

    set year(value: string) {
        this._year = value;
    }

    get filename(): string {
        return this._filename;
    }

    set filename(value: string) {
        this._filename = value;
    }

    get duration(): string {
        return this._duration;
    }

    set duration(value: string) {
        this._duration = value;
    }

    get rating(): number {
        return this._rating;
    }

    set rating(value: number) {
        this._rating = value;
    }


    get tempRating(): number {
        return this._tempRating;
    }

    set tempRating(value: number) {
        this._tempRating = value;
    }

    toObject() {
        return {
            id: this._id,
            title: this._title,
            artist: this._artist,
            album: this._album,
            year: this._year,
            filename: this._filename,
            duration: this._duration,
            rating: this._rating,
            tempRating: this._tempRating
        };
    }
}
