import {Drink} from '../../dtos/drink';
import {DrinkService} from '../../services/drink.service';
import {ChangeDetectorRef, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';

import {Category} from '../../dtos/category';
import {CategoryService} from '../../services/category.service';
import Swal from 'sweetalert2';
import {Food} from '../../dtos/food';

@Component({
  selector: 'app-drink',
  templateUrl: './drink.component.html',
  styleUrls: ['./drink.component.scss']
})
export class DrinkComponent implements OnInit {
  @Input() category: number;
  @ViewChild('closeUpdateModal', null) closeUpdateModal: ElementRef;
  @Output() addDrinkToCartOutput = new EventEmitter<Drink>();

  createForm: FormGroup;
  updateForm: FormGroup;

  submittedCreate: boolean = false;
  submittedUpdate: boolean = false;

  private drink: Drink[];
  selectedDrink: Drink = new Drink(0, ' ', '', 0, 0, '', '', [], false, false);


  successText: string = '';
  errorText: string = '';

  findDrinksText: string = '';

  allCategories: Category[];
  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};
  constructor(private drinkService: DrinkService, private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef, private authService: AuthService, private categoryService: CategoryService) {
    this.createForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: ['', [Validators.required]],
      alcohol: ['', [Validators.required]],
      image: ['', [Validators.required]],
      imageContentType: ['', [Validators.required]]
    });
    this.updateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: ['', [Validators.required]],
      alcohol: ['', [Validators.required]],
      image: ['', [Validators.required]],
      imageContentType: ['', [Validators.required]]
    });

    this.dropdownList = [];
    this.selectedItems = [];
    this.allCategories = [];

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'name',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };
  }

  selectDrink(drink: Drink) {
    // old drink where only updated is set to true
    this.selectedDrink = new Drink(drink.id, drink.name, drink.description, drink.price, drink.alcohol, drink.image, drink.imageContentType, drink.categoryIds, drink.deleted, drink.updated);

    // create the new drink with the changes
    this.updateForm.patchValue({'id': drink.id});
    this.updateForm.patchValue({'name': drink.name});
    this.updateForm.patchValue({'description': drink.description});
    this.updateForm.patchValue({'alcohol': drink.alcohol});
    this.updateForm.patchValue({'price': drink.price});
    this.updateForm.patchValue({'image': drink.image});
    this.updateForm.patchValue({'imageContentType': drink.imageContentType});
  }

  ngOnInit() {
    this.loadDrinks();
    this.loadCategories();
  }
  onItemSelect(item: any) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }

  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  isUser(): boolean {
    return this.authService.getUserRole() === 'USER';
  }

  getDrink(): Drink[] {
    return this.drink;
  }

  private clearForm() {
    this.createForm.reset();
    this.updateForm.reset();
    this.submittedCreate = false;
    this.submittedUpdate = false;
    this.selectedItems = [];
  }

  private loadDrinks() {

    if (this.category != null) {
      this.drinkService.getDrinkById(this.category)
        .subscribe(
          (drink: Drink[]) => {
            this.drink = drink;
          },
          error => {
            this.defaultServiceErrorHandling(error, '');
          }
        );
    } else {
      this.drinkService.listDrink().subscribe(
        (drink: Drink[]) => {
          this.drink = drink;
        },
        error => {
          this.defaultServiceErrorHandling(error, '');
        }
      );
    }
  }

  loadDrinksByName() {


    if (this.category != null) {
      this.drinkService.findDrinksByNameAndID(this.findDrinksText, this.category).subscribe(
        (drink: Drink[]) => {
          this.drink = drink;
        },
        error => {
          this.defaultServiceErrorHandling(error, '');
        }
      );
    } else {
      this.drinkService.findDrinksByName(this.findDrinksText).subscribe(
        (drink: Drink[]) => {
          this.drink = drink;
        },
        error => {
          this.defaultServiceErrorHandling(error, '');
        }
      );
    }


  }

  createDrink() {
    this.submittedCreate = true;
    if (this.createForm.valid) {
      const drink: Drink = new Drink(null,
        this.createForm.controls.name.value,
        this.createForm.controls.description.value,
        this.createForm.controls.price.value,
        this.createForm.controls.alcohol.value,
        this.createForm.controls.image.value,
        this.createForm.controls.imageContentType.value,
        this.listSelectedCatIds(),
        false,
        false
      );
      this.sendCreatedDrinkToService(drink, 'create');
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  updateDrink(output: string) {
    this.submittedUpdate = true;
    if (this.updateForm.valid) {
      // this will be the new drink created
      const drink: Drink = new Drink(this.selectedDrink.id,
        this.updateForm.controls.name.value,
        this.updateForm.controls.description.value,
        this.updateForm.controls.price.value,
        this.updateForm.controls.alcohol.value,
        this.updateForm.controls.image.value,
        this.updateForm.controls.imageContentType.value,
        this.listSelectedCatIds(),
        false,
        false
      );
      // this will be the old drink where we only change the value updated to true
      const drink1: Drink = new Drink(this.selectedDrink.id,
        this.selectedDrink.name,
        this.selectedDrink.description,
        this.selectedDrink.alcohol,
        this.selectedDrink.price,
        this.selectedDrink.image,
        this.selectedDrink.imageContentType,
        this.listSelectedCatIds(),
        this.selectedDrink.deleted,
        true
      );
      this.sendCreatedDrinkToService(drink, 'update');
      this.sendUpdatedDrinkToService(drink1, 'update');
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {

          this.selectedDrink.imageContentType = file.type;

          this.createForm.patchValue({
            imageContentType: file.type
          });

          this.updateForm.patchValue({
            imageContentType: file.type
          });
          console.log('filetype ' + file.type);
          this.toBase64FromFile(file);
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
    );
  }

  toBase64FromFile(file) {
    if (file) {
      const reader = new FileReader();
      reader.onload = this._handleReaderLoaded.bind(this);
      reader.readAsBinaryString(file);
    }
  }

  _handleReaderLoaded(readerEvt) {
    const binaryString = readerEvt.target.result;
    // console.log("Fehler: "+ file.type);
    this.selectedDrink.image = btoa(binaryString);
    this.createForm.patchValue({
      image: btoa(binaryString)
    });
    this.updateForm.patchValue({
      image: btoa(binaryString)
    });
    console.log(btoa(binaryString));
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.createForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
  }

  sendCreatedDrinkToService(drink: Drink, output: string) {
    this.drinkService.createDrink(drink).subscribe(
      () => {
        this.loadDrinks();
        this.defaultServiceSuccessHandling(output);
      },
      error => {
        this.defaultServiceErrorHandling(error, output);
      }
    );
  }

  sendUpdatedDrinkToService(drink: Drink, output: string) {
    this.drinkService.updateDrink(drink).subscribe(
      () => {
        this.loadDrinks();
        this.defaultServiceSuccessHandling(output);
      },
      error => {
        this.defaultServiceErrorHandling(error, output);
      }
    );
  }

  confirmDeleteDrink(drink: Drink): void {
    Swal.fire({
      title: 'Artikel löschen?',
      text: 'Möchten sie ' + drink.name + ' wirklich löschen?',
      icon: 'warning',
      confirmButtonText: 'Ja',
      cancelButtonText: 'Abbrechen',
      showCancelButton: true
    }).then(result => {
      if (result.value) {
        this.updateVarDeleted(drink);
      }
    });
  }

  updateVarDeleted(drink: Drink) {
    drink.deleted = true;
    this.sendUpdatedDrinkToService(drink, 'delete');
  }

  deleteDrink(id: number): void {
    this.drinkService.deleteDrink(id).subscribe(
      () => {
        this.loadDrinks();
        this.defaultServiceSuccessHandling('delete');
      },
      error => {
        this.defaultServiceErrorHandling(error, 'delete');
      }
    );
  }

  addDrinkToCart(drink: Drink): void {
    console.log(this.selectedDrink);
    this.addDrinkToCartOutput.emit(drink);
  }
  private defaultServiceErrorHandling(error: any, action: string) {
    if (action === 'update') {
      this.errorText = 'Getränk konnte nicht aktualisiert werden';
    } else if (action === 'create') {
      this.errorText = 'Getränk konnte nicht erstellt erstellt';
    } else if (action === 'delete') {
      this.errorText = 'Getränk konnte nicht gelöscht werden';
    } else if (action === 'get'){
      this.errorText = 'Getränk konnte nicht geladen werden';
    }

    console.log(error);
    this.closeUpdateModal.nativeElement.click();
    Swal.fire(
      'Es gab ein Problem!',
      this.errorText,
      'question'
    );
  }

  private loadCategories() {
    console.log('getting categories');
    this.categoryService.getAllCategories().subscribe((cat: Category[]) => {
        this.allCategories = cat;
        console.log(this.allCategories.length);
        this.dropdownList = this.allCategories;
      }, error => {
        this.defaultServiceErrorHandling(error, 'get');
      }
    );

  }

  private listSelectedCatIds(): number[] {
    let ids = [];
    let i;
    for (i = 0; i < this.selectedItems.length; i++) {
      ids = ids.concat(this.selectedItems[i].id);
    }
    return ids;
  }

  private defaultServiceSuccessHandling(action: String) {
    this.closeUpdateModal.nativeElement.click();
    if (action === 'update') {
      this.successText = 'Getränk wurde erfolgreich aktualisiert';
    } else if (action === 'create') {
      this.successText = 'Getränk wurde erfolgreich erstellt';
    } else if (action === 'delete') {
      this.successText = 'Getränk wurde erfolgreich gelöscht';
    }

    Swal.fire({
      title: 'Wunderbar!',
      text: this.successText
    });

    console.log('Successfully ' + this.successText + 'drink');
  }

  private setAssignedCats(drink: Drink): void {
    let i;
    let j;
    this.selectedItems = [];
    for (i = 0; i < drink.categoryIds.length; i++) {
      for (j = 0; j < this.allCategories.length; j++) {
        if (drink.categoryIds[i] === this.allCategories[j].id) {
          this.selectedItems = this.selectedItems.concat(this.allCategories[j]);
        }
      }
    }
  }

  private getDrinkCategories(drink: Drink): Category[] {
    let cats = [];
    let i;
    let j;
    for (i = 0; i < drink.categoryIds.length; i++) {
      for (j = 0; j < this.allCategories.length; j++) {
        if (drink.categoryIds[i] === this.allCategories[j].id){
          cats = cats.concat(this.allCategories[j]);
        }
      }
    }
    return cats;
  }
}
