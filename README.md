# Material Animations

Exemplo de utilização de animações e transições no Android.

### 1. Alterando propriedade do elemento

Alterar o atributo de largura para torná-lo menor desencadeará um layoutMeasure. Nesse ponto, o quadro de transição irá gravar valores de início e término e criará uma animação para a transição de um para outro.

```java
public void moveImage(View view) {
     imageView = (View) findViewById(R.id.ic_launcher);
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
         TransitionManager.beginDelayedTransition(background);
         if (side) {
             imageView.setLayoutParams(alignRight());
             this.side = false;
         } else {
             imageView.setLayoutParams(alignLeft());
             this.side = true;
         }
}

public RelativeLayout.LayoutParams alignLeft() {
     RelativeLayout.LayoutParams options = new RelativeLayout.LayoutParams(192, 192);
     options.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
     return options;
}

public RelativeLayout.LayoutParams alignRight() {
      RelativeLayout.LayoutParams options = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, 
                RelativeLayout.LayoutParams.WRAP_CONTENT);
      options.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
      return options;
}
```
![change_and_move_animation](https://cloud.githubusercontent.com/assets/4808317/20438817/195464ca-ada0-11e6-95e2-6056968c8526.gif)

### 2. Animação de elementos no layout

#### Scene
Transition Framework pode ser usado para animar mudanças de elementos dentro do layout de atividade atual.

Transições acontecem entre cenas. Uma cena é apenas um layout regular que define um estado estático da nossa interface do usuário. Você pode fazer a transição de uma cena para outra e o Framework de Transição animará as vistas entre elas.

``` java
view_rl = (ViewGroup) findViewById(R.id.view_rl);
if (Build.VERSION.SDK_INT >= 19) {
    scene1 = Scene.getSceneForLayout(view_rl, R.layout.activity_scene_1, this);
    scene2 = Scene.getSceneForLayout(view_rl, R.layout.activity_scene_2, this);
}

(...)

public void clickChangeAnimations(View v) {
      if (Build.VERSION.SDK_INT >= 19) {
          if (state) {
               TransitionManager.go(scene1, new ChangeBounds());
               state = false;
           } else {
               TransitionManager.go(scene2, new ChangeBounds());
               state = true;
           }
       }
 }
```
![scenes_animation](https://cloud.githubusercontent.com/assets/4808317/20439769/5e49df52-ada4-11e6-8b98-b0979a4e2383.gif)

### 3. Transições entre layouts

Quando a transição da `atividade A` para o layout de conteúdo da `Atividade B` é animada de acordo com a transição definida. Existem três transições predefinidas disponíveis no `android.transition`.Transition você pode usar: **Explode**, **Slide** e **Fade**. Todas essas transições controlam as alterações na visibilidade das visualizações de destino no layout da atividade e animam essas exibições para seguir as regras de transição.

Você pode definir essas transições declarativas usando `XML` ou programaticamente. Exemplo:
#### Declaração

Transições são definidas em `res/transition`

> res/transition/fade.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<fade xmlns:android="http://schemas.android.com/apk/res/"
    android:duration="500"/>

```

> res/transition/slide.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<slide xmlns:android="http://schemas.android.com/apk/res/"
    android:duration="500"/>

```
Como utilizar as transitions animations:

> MainActivity.java

```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        //TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             Explode explode = new Explode();
             Slide slide = new Slide();
             
             getWindow().setReenterTransition(explode);
             getWindow().setExitTransition(slide);
        }
    }
```

> ActivityB.java

```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        //TRANSITIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            // Fade fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
            Slide slide = new Slide();
            // Slide slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
            
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(slide);
        }
    }
```

### 4. Compartilhar elementos entre Activity's

A idéia por trás disso é ter duas visões diferentes em dois layouts diferentes e vinculá-los de alguma forma com uma animação.
A estrutura de transição fará então todas as animações que considere necessárias para mostrar ao usuário uma transição de uma visão para outra.
Manter isto sempre na mente: a vista não está movendo realmente de uma disposição a outra. São duas vistas independentes.

### a) É necessário habilitar o conteúdo de transição no app `styles.xml` .

> values/styles.xml

```xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
      ...
      <item name="android:windowContentTransitions">true</item>
</style>
```
### b) Definir um nome de transação em comum.

Para fazer o efeito você precisa dar tanto, origem e visões de destino, o mesmo `android:transitionName`. Eles podem ter ids ou propriedades diferentes, mas `android:transitionName` deve ser o mesmo.

> layout/activity_main.xml

```xml
 <ImageView
            android:id="@+id/ic_launcher"
            ...
            android:src="@drawable/circle"
            android:tint="@android:color/holo_red_dark"
            android:transitionName="@string/circle_name" />
```

> layout/activity_b.xml

```xml
  <ImageView
        android:id="@+id/ic_launcher"
        ...
        android:transitionName="@string/circle_name"
        android:src="@drawable/circle" />
```

### b) Iniciar a activity

Use o método `ActivityOptions.makeSceneTransitionAnimation()` para definir a exibição de origem do elemento compartilhado e o nome da transição.

> MainActivity.java

```java
public void startActivity(View View) {
        Intent it = new Intent(this, ActivityB.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView = (View) findViewById(R.id.ic_launcher);

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, 
                                                  imageView, 
                                                  getResources().getString(R.string.circle_name));

            this.startActivity(it, optionsCompat.toBundle());
        } else {
            startActivity(it);
        }
    }

```

![transition_sharedelement](https://cloud.githubusercontent.com/assets/4808317/20440105/bb4180a6-ada5-11e6-978b-5d277c436433.gif) | 
![transition_sharedelement2](https://cloud.githubusercontent.com/assets/4808317/20440108/bd93565e-ada5-11e6-8ebd-0e4b45e66a9a.gif)

