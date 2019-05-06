import { Track } from './track';

export class Playlist {
    private _id: number;
    private _name: string;
    private _tracks: Track[] = [];

    constructor(data: any) {
        this._id = data.id;
        this._name = data.name;
        data.tracks.forEach(track => {
            this._tracks.push(new Track(track));
        });
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get name(): string {
        return this._name;
    }

    set name(value: string) {
        this._name = value;
    }

    get tracks(): Track[] {
        return this._tracks;
    }

    set tracks(value: Track[]) {
        this._tracks = value;
    }
}
