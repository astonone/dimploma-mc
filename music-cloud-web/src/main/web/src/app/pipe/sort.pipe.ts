import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
    name: 'sortById'
})
export class SortPipe implements PipeTransform {

    private name: any;

    transform(array: any, args: any): any {
        array.sort((a: any, b: any) =>
            a.id - b.id
        );
        return array;
    }
}