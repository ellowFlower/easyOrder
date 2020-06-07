import {Food} from '../../dtos/food';
import {FoodService} from '../../services/food.service';
import {ChangeDetectorRef, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Category} from '../../dtos/category';
import {CategoryService} from '../../services/category.service';
import Swal from 'sweetalert2';
import {isAngularCoreReference} from '@angular/compiler-cli/src/ngtsc/annotations/src/util';
import {Order} from '../../dtos/order';

@Component({
  selector: 'app-food',
  templateUrl: './food.component.html',
  styleUrls: ['./food.component.scss']
})

export class FoodComponent implements OnInit {
  @Input() category: number;
  @ViewChild('closeUpdateModal', null) closeUpdateModal: ElementRef;
  @Output() addFoodToCartOutput = new EventEmitter<Food>();

  createForm: FormGroup;
  updateForm: FormGroup;

  submittedCreate: boolean = false;
  submittedUpdate: boolean = false;

  private food: Food[];
  selectedFood: Food = new Food(0, ' ', '', 0, '', '', [], false, false);

  successText: string = '';
  errorText: string = '';

  findFoodsText: string = '';

  allCategories: Category[];
  dropdownList = [];
  selectedItems = [];
  dropdownSettings = {};


  constructor(private foodService: FoodService, private ngbPaginationConfig: NgbPaginationConfig, private formBuilder: FormBuilder,
              private cd: ChangeDetectorRef, private authService: AuthService, private categoryService: CategoryService) {
    this.createForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: ['', [Validators.required]],
      image: ['', [Validators.required]],
      imageContentType: ['', [Validators.required]]
    });
    this.updateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: ['', [Validators.required]],
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

  selectFood(food: Food) {
    // old food where only updated is set to true
    this.selectedFood = new Food(food.id,
      food.name,
      food.description,
      food.price,
      food.image,
      food.imageContentType,
      food.categoryIds,
      food.deleted,
      food.updated);

    // create the new food with the changes
    this.updateForm.patchValue({'id': food.id});
    this.updateForm.patchValue({'name': food.name});
    this.updateForm.patchValue({'description': food.description});
    this.updateForm.patchValue({'price': food.price});
    this.updateForm.patchValue({'image': food.image});
    this.updateForm.patchValue({'imageContentType': food.imageContentType});
    this.setAssignedCats(food);
  }

  ngOnInit() {
    this.loadFoods();
    this.loadCategories();
  }
  onItemSelect(item: any) {
    console.log(item);
  }
  onSelectAll(items: any) {
    console.log(items);
  }
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }
  isUser(): boolean {
    return this.authService.getUserRole() === 'USER';
  }
  getFood(): Food[] {
    return this.food;
  }

  private clearForm() {
    this.createForm.reset();
    this.updateForm.reset();
    this.submittedCreate = false;
    this.submittedUpdate = false;
    this.selectedItems = [];
  }

  private loadFoods() {
    if (this.category != null) {
      this.foodService.getFoodById(this.category)
        .subscribe(
          (food: Food[]) => {
            this.food = food;
          },
          error => {
            this.defaultServiceErrorHandling(error, '');
          }
        );
    } else {
      this.foodService.listFood()
        .subscribe(
          (food: Food[]) => {
            this.food = food;
          },
          error => {
            this.defaultServiceErrorHandling(error, '');
          }
        );
    }
  }

  loadFoodsByName() {
    if (this.category != null) {
      this.foodService.findFoodsByNameAndID(this.findFoodsText, this.category).subscribe(
        (food: Food[]) => {
          this.food = food;
        },
        error => {
          this.defaultServiceErrorHandling(error, '');
        }
      );
    } else {
      this.foodService.findFoodsByName(this.findFoodsText).subscribe(
        (food: Food[]) => {
          this.food = food;
        },
        error => {
          this.defaultServiceErrorHandling(error, '');
        }
      );
    }
  }

  createFood() {
    this.submittedCreate = true;
    if (this.createForm.valid) {
      const food: Food = new Food(null,
        this.createForm.controls.name.value,
        this.createForm.controls.description.value,
        this.createForm.controls.price.value,
        this.createForm.controls.image.value,
        this.createForm.controls.imageContentType.value,
        this.listSelectedCatIds(),
        false,
        false
      );
      this.sendCreatedFoodToService(food, 'create');
      this.clearForm();
    } else {
      console.log('Invalid input');
    }
  }

  updateFood(output: string) {
    this.submittedUpdate = true;
    if (this.updateForm.valid) {
      // this will be the new food created
      const food: Food = new Food(this.selectedFood.id,
        this.updateForm.controls.name.value,
        this.updateForm.controls.description.value,
        this.updateForm.controls.price.value,
        this.updateForm.controls.image.value,
        this.updateForm.controls.imageContentType.value,
        this.listSelectedCatIds(),
        false,
        false
      );
      // this will be the old food where we only change the value updated to true
      const food1: Food = new Food(this.selectedFood.id,
        this.selectedFood.name,
        this.selectedFood.description,
        this.selectedFood.price,
        this.selectedFood.image,
        this.selectedFood.imageContentType,
        this.listSelectedCatIds(),
        this.selectedFood.deleted,
        true
      );
      this.sendCreatedFoodToService(food, 'update');
      this.sendUpdatedFoodToService(food1, 'update');
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

          this.selectedFood.imageContentType = file.type;

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

    this.selectedFood.image = btoa(binaryString);

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


  sendCreatedFoodToService(food: Food, output: string) {
    this.foodService.createFood(food).subscribe(
      () => {
        this.loadFoods();
        this.defaultServiceSuccessHandling(output);
      },
      error => {
        this.defaultServiceErrorHandling(error, output);
      }
    );
  }

  sendUpdatedFoodToService(food: Food, output: string) {
    this.foodService.updateFood(food).subscribe(
      () => {
        this.loadFoods();
        this.defaultServiceSuccessHandling(output);
      },
      error => {
        this.defaultServiceErrorHandling(error, output);
      }
    );
  }

  confirmDeleteFood(food: Food): void {
    Swal.fire({
      title: 'Artikel löschen?',
      text: 'Möchten sie ' + food.name + ' wirklich löschen?',
      icon: 'warning',
      confirmButtonText: 'Ja',
      cancelButtonText: 'Abbrechen',
      showCancelButton: true
    }).then(result => {
      if (result.value) {
        this.updateVarDeleted(food);
      }
    });
  }

  updateVarDeleted(food: Food) {
      food.deleted = true;
      this.sendUpdatedFoodToService(food, 'delete');
  }

  deleteFood(id: number): void {
    this.foodService.deleteFood(id).subscribe(
      () => {
        this.loadFoods();
        this.defaultServiceSuccessHandling('delete');
      },
      error => {
        this.defaultServiceErrorHandling(error, 'delete');
      }
    );
  }

  addFoodToCart(food: Food): void {
    console.log(this.selectedFood);
    this.addFoodToCartOutput.emit(food);
  }

  private defaultServiceErrorHandling(error: any, action: string) {
    if (action === 'update') {
      this.errorText = 'Gericht konnte nicht aktualisiert werden';
    } else if (action === 'create') {
      this.errorText = 'Gericht konnte nicht erstellt erstellt';
    } else if (action === 'delete') {
      this.errorText = 'Gericht konnte nicht gelöscht werden';
    } else if (action === 'get') {
      this.errorText = 'Gericht konnte nicht geladen werden';
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

  private setAssignedCats(food: Food): void {
    let i;
    let j;
    this.selectedItems = [];
    for (i = 0; i < food.categoryIds.length; i++) {
      for (j = 0; j < this.allCategories.length; j++) {
        if (food.categoryIds[i] === this.allCategories[j].id) {
          this.selectedItems = this.selectedItems.concat(this.allCategories[j]);
        }
      }
    }
  }

  private getFoodCategories(food: Food): Category[] {
    let cats = [];
    let i;
    let j;
    for (i = 0; i < food.categoryIds.length; i++) {
      for (j = 0; j < this.allCategories.length; j++) {
        if (food.categoryIds[i] === this.allCategories[j].id) {
          cats = cats.concat(this.allCategories[j]);
        }
      }
    }
    return cats;
  }

  private defaultServiceSuccessHandling(action: String) {
    this.closeUpdateModal.nativeElement.click();
    if (action === 'update') {
      this.successText = 'Gericht wurde erfolgreich aktualisiert';
    } else if (action === 'create') {
      this.successText = 'Gericht wurde erfolgreich erstellt';
    } else if (action === 'delete') {
      this.successText = 'Gericht wurde erfolgreich gelöscht';
    }

    Swal.fire({
      title: 'Wunderbar!',
      text: this.successText
    });

    console.log('Successfully ' + this.successText + 'food');
  }
}
