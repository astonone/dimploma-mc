import { Playlist } from './playlist';

export class PlaylistList {
    private _allCount : number;
    private _playlists : Playlist[] = [];

    constructor(data: any) {
        this._allCount = data.allCount;
        data.playlists.forEach(playlist => {
            this._playlists.push(new Playlist(playlist));
        });
    }

    get allCount(): number {
        return this._allCount;
    }

    set allCount(value: number) {
        this._allCount = value;
    }

    get playlists(): Playlist[] {
        return this._playlists;
    }

    set playlists(value: Playlist[]) {
        this._playlists = value;
    }
}
