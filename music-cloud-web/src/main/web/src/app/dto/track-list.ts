import {Track} from './track';

export class TrackList {
    private _tracks : Track[] = [];
    private _allCount : number;

    constructor(data: any) {
        for (let i = 0; i < data.tracks.length; i++) {
            this._tracks.push(new Track(data.tracks[i]));
        }
        this._allCount = data.countAll;
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
