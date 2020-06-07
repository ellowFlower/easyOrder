import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {RegisterUserRequest} from '../../dtos/registerUser-request';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  @ViewChild('closeUpdateModal', null) closeUpdateModal: ElementRef;
  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted: boolean = false;
  // Error flag
  error: boolean = false;
  errorMessage: string = '';

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: [''],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  registerUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      // tslint:disable-next-line:no-shadowed-variable
      const registerUserRequest: RegisterUserRequest = new RegisterUserRequest(this.registerForm.controls.name.value,
        this.registerForm.controls.email.value,
        this.registerForm.controls.password.value);
      this.authenticateUser(registerUserRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the login page
   * @param authRequest authentication data from the user login form
   */
  // tslint:disable-next-line:no-shadowed-variable
  authenticateUser(registerUserRequest: RegisterUserRequest) {
    console.log('Try to register user: ' + registerUserRequest.email + registerUserRequest.name, registerUserRequest.password);
    this.authService.registerUser(registerUserRequest).subscribe(
      () => {
        console.log('Successfully registered in user: ' + registerUserRequest.email);
        this.defaultServiceSuccessHandling();
        this.router.navigate(['/login']);
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
      }
    );
  }

  private defaultServiceSuccessHandling() {
    this.closeUpdateModal.nativeElement.click();

    Swal.fire({
      title: 'Wunderbar!',
      text: 'Registrierung erfolgreich'
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
  }

}
