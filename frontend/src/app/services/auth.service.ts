import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import * as jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';
import {RegisterUserRequest} from '../dtos/registerUser-request';
import {RegisterTableRequest} from '../dtos/registerTable-request';
import {OrderFindAll} from '../dtos/orderFindAll';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';
  private authBaseUri2: string = this.globals.backendUri + '/registration';
  private authBaseUriRegisterTable: string = this.authBaseUri2 + '/tables';
  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Load all users without admins
   */
  getTables(): Observable<RegisterTableRequest[]> {
    return this.httpClient.get<RegisterTableRequest[]>(this.authBaseUri2);
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }

  registerUser(registerUserRequest: RegisterUserRequest): Observable<string> {
  return this.httpClient.post(this.authBaseUri2, registerUserRequest, {responseType: 'text'})
      .pipe(
        tap((registerUserResponse: string) => this.setToken(registerUserResponse))
      );
  }

  registerTable(registerTableRequest: RegisterTableRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUriRegisterTable, registerTableRequest, {responseType: 'text'});
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return !!this.getToken() && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  public getTokenUserId() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const userId = decoded['userid'];

      return userId;
    }

    return null;
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date {

    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  deleteTable(table: RegisterTableRequest): Observable<any> {
    return this.httpClient.delete(this.authBaseUri2 + '/' + table.name);
  }

}
