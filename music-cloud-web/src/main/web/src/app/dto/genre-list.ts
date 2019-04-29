import { Genre } from './genre';

export class GenreList {
    private _allCount: number;
    private _genres: Genre[] = [];

    constructor(data: any) {
        this._allCount = data.allCount;
        data.genres.forEach(genre => {
            this._genres.push(new Genre(genre));
        });
    }

    get allCount(): number {
        return this._allCount;
    }

    set allCount(value: number) {
        this._allCount = value;
    }

    get genres(): Genre[] {
        return this._genres;
    }

    set genres(value: Genre[]) {
        this._genres = value;
    }
}
