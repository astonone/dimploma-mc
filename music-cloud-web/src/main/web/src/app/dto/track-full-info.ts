import { Observable } from 'rxjs';
import { Genre } from './genre';
import { Mood } from './mood';

export class TrackFullInfo {
    private _id: number;
    private _title: string;
    private _artist: string;
    private _album: string;
    private _year: string;
    private _filename: string;
    private _files: Observable<string[]>;
    private _duration: string;
    private _rating: number;
    private _tempRating: number;
    private _genres: Genre[] = [];
    private _moods: Mood[] = [];

    public static createEmptyObject() {
        return new TrackFullInfo({
           id: '',
           title: '',
           artist: '',
           album: '',
           year: '',
           filename: '',
           duration: '',
           rating: '',
           genres: [],
           moods: []
        });
    }

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
        this._files = null;

        data.genres.forEach(genre => {
            this._genres.push(new Genre(genre));
        });

        data.moods.forEach(mood => {
            this._moods.push(new Mood(mood));
        });
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

    get files(): Observable<string[]> {
        return this._files;
    }

    set files(value: Observable<string[]>) {
        this._files = value;
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

    get genres(): Genre[] {
        return this._genres;
    }

    set genres(value: Genre[]) {
        this._genres = value;
    }

    get moods(): Mood[] {
        return this._moods;
    }

    set moods(value: Mood[]) {
        this._moods = value;
    }
}
