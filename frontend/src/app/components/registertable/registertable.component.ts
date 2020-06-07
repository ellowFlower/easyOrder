import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {RegisterTableRequest} from '../../dtos/registerTable-request';
import Swal from "sweetalert2";
import {Observable} from 'rxjs';
import {OrderFindAll} from '../../dtos/orderFindAll';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Food} from '../../dtos/food';

@Component({
  selector: 'app-registertable',
  templateUrl: './registertable.component.html',
  styleUrls: ['./registertable.component.scss']
})
export class RegistertableComponent implements OnInit {

  @ViewChild('closeUpdateModal', null) closeUpdateModal: ElementRef;
  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted: boolean = false;
  // Error flag
  error: boolean = false;
  errorMessage: string = '';
  private registerTableRequests: RegisterTableRequest[];
  errorText: string = '';
  successText: string = '';


  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router, private httpClient: HttpClient, private globals: Globals) {
    this.registerForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: [''],
      password: ['', [Validators.required, Validators.minLength(8)]],
      seats: ['', [Validators.required]]
    });
  }

  /**
  * Form validation will start after the method is called, additionally an AuthRequest will be sent
  */
  registerTable() {
    this.submitted = true;
    if (this.registerForm.valid) {
      // tslint:disable-next-line:no-shadowed-variable
      const registerTableRequest: RegisterTableRequest = new RegisterTableRequest(this.registerForm.controls.name.value,
        this.registerForm.controls.email.value,
        this.registerForm.controls.password.value,
        this.registerForm.controls.seats.value);
      this.authenticateTable(registerTableRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   * @param authRequest authentication data from the user login form
   */
  // tslint:disable-next-line:no-shadowed-variable
  authenticateTable(registerTableRequest: RegisterTableRequest) {
    // tslint:disable-next-line:max-line-length
    console.log('Try to register table: ' + registerTableRequest.email + registerTableRequest.name, registerTableRequest.password, registerTableRequest.seats);
    this.authService.registerTable(registerTableRequest).subscribe(
      () => {
        console.log('Successfully registered in user: ' + registerTableRequest.email);
        this.router.navigate(['/registertable']);
        this.defaultServiceSuccessHandling('register');
        this.loadTables();
      },
      error => {
        console.log('Could not log in due to:');
        console.log(error);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
    });
  }

  private defaultServiceSuccessHandling(action: String) {
    this.closeUpdateModal.nativeElement.click();
    if (action === 'register') {
      this.successText = 'Registrierung erfolgreich';
    } else if (action === 'delete') {
      this.successText = 'Tisch wurde erfolgreich gelöscht';
    }

    Swal.fire({
      title: 'Wunderbar!',
      text: this.successText
    });

    console.log('Successfull registration');
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }
  ngOnInit() {
    this.loadTables();
  }

  private loadTables() {
    this.authService.getTables().subscribe(
      (registerTableRequests: RegisterTableRequest[]) => {
        this.registerTableRequests = registerTableRequests;
      }
    );
  }

  confirmDeleteTable(table: RegisterTableRequest): void {
    Swal.fire({
      title: 'Tisch löschen?',
      text: 'Möchten sie ' + table.name + ' wirklich löschen?',
      icon: 'warning',
      confirmButtonText: 'Ja',
      cancelButtonText: 'Abbrechen',
      showCancelButton: true
    }).then(result => {
      if (result.value) {
        this.deleteTable(table);
      }
    });
  }

  deleteTable(table: RegisterTableRequest): void {
    this.authService.deleteTable(table).subscribe(
      () => {
        this.loadTables();
        this.defaultServiceSuccessHandling('delete');
      },
      error => {
        this.defaultServiceErrorHandling(error, 'delete');
      }
    );
  }

  private defaultServiceErrorHandling(error: any, action: string) {
    if (action === 'update') {
      this.errorText = 'Tisch konnte nicht aktualisiert werden';
    } else if (action === 'create') {
      this.errorText = 'Tisch konnte nicht erstellt erstellt';
    } else if (action === 'delete') {
      this.errorText = 'Tisch konnte nicht gelöscht werden. Möglicherweise gibt es noch offene Bestellungen.';
    } else if (action === 'get') {
      this.errorText = 'Tisch konnte nicht geladen werden';
    }
    console.log(error);

    this.closeUpdateModal.nativeElement.click();
    Swal.fire(
      'Es gab ein Problem!',
      this.errorText,
      'question'
    );
  }

}
