import {Track} from './track';

export class TrackList {
    private _tracks : Track[] = [];
    private _allCount : number;

    constructor(data: any) {
        this._allCount = data.countAll;
        data.tracks.forEach(track => {
            this._tracks.push(new Track(track));
        });
    }

    get tracks(): Track[] {
        return this._tracks;
    }

    set tracks(value: Track[]) {
        this._tracks = value;
    }

    get allCount(): number {
        return this._allCount;
    }

    set allCount(value: number) {
        this._allCount = value;
    }
}
