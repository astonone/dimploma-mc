import { NgModule }      from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';;
import { MainUiModule } from './modules/main-ui/main-ui.module'
import { UserService } from './services/user.service'

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        routing,
        BrowserAnimationsModule,
        MainUiModule    ],
    declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent
    ],
    providers: [
        UserService
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }