$(document).ready(function () {
  var windowRelatedHash = $(window).width();
  var dataHashtag;
  var shuffleAll;

  // shuffle the cards
  function shuffle(array) {
    let currentIndex = array.length,  randomIndex;
  
    // While there remain elements to shuffle...
    while (currentIndex != 0) {
  
      // Pick a remaining element...
      randomIndex = Math.floor(Math.random() * currentIndex);
      currentIndex--;
  
      // And swap it with the current element.
      [array[currentIndex], array[randomIndex]] = [
        array[randomIndex], array[currentIndex]];
    }
  
    return array;
  }

  $(document).on('click', '#shuffle', function () {
    if(shuffleAll === 'true || false'){
      setSwiperStructure(dataHashtag);
    }

  });
  


  // Hashtag Click
  $(document).on("click", ".relatedHashtag .bar__item", function () {
    if (!$(this).hasClass("bar__item--active")) {
      $(".relatedHashtag .bar__item").removeClass("bar__item--active");
      $(this).addClass("bar__item--active");
      $(".relatedHashtag__result").fadeIn();
      var parm = $(this).attr("data-hashtag");
      var max = $(this).attr("data-maxarticle");
      var shuffleAtt = $(this).attr("data-shuffle");
      shuffleAll = shuffleAtt;
      getResult(parm, max);
    }
  });

  // Close Click
  $(".icon-close").click(function () {
    $(".relatedHashtag__result").fadeOut();
    $(".relatedHashtag .bar__item").removeClass("bar__item--active");
  })

  //RESIZE MANAGEMENT
  $(window).on("resize", function () {
    if ($(window).width() !== windowRelatedHash) {
      windowRelatedHash = $(window).width();
      if ($(window).width() < 1025 && (!$(".result__cards .swiper-wrapper").hasClass("mobile"))) {
        $(".result__cards .swiper-wrapper").addClass("mobile").removeClass("desktop");
        setSwiperStructure(dataHashtag);
        cardsSwiper()
      }
      if ($(window).width() >= 1025 && (!$(".result__cards .swiper-wrapper").hasClass("desktop"))) {
        $(".result__cards .swiper-wrapper").addClass("desktop").removeClass("mobile");
        setSwiperStructure(dataHashtag);
        cardsSwiper()
      }

    }
  });

  /****************************************************
   * FUNCTION
   ****************************************************/

  function cardsSwiper() {
    if ($(".relatedHashtag .swiper-slide-active").length > 0) {
      $(".relatedHashtag .swiper-container")[0].swiper.destroy();
      addBg($(window).width());
    }

    var cardsSwiper = new Swiper(".result__cards .swiper-container", {
      // Optional parameters
      spaceBetween: 10,
      slidesPerView: 1,
      paginationClickable: true,
      autoResize: true,
      pagination: {
        el: ".result__cards .swiper-pagination",
        clickable: true,
      },
    });

    // Clean single swiper bullet
    var slide = $(".relatedHashtag").find(".swiper-slide");
    if (slide.length == 1) {
      $(".relatedHashtag .result__cards .swiper-pagination").hide();
    } else {
      $(".relatedHashtag .result__cards .swiper-pagination").show();
    }
  }

  function setSwiperStructure(data) {
    if(shuffleAll === 'true || false'){
      data = shuffle(data);
    }
    $(".relatedHashtag__result .swiper-slide").remove();
    if (data) {
      $(".result__number .number").text(data.length);

      var emptyCard =
        "<a class='card' href=''>" +
        "<div class='card__bg'" +
        "data-bg=''" +
        "data-bg-mobile=''>" +
        "</div>" +
        "<div class='card__hashtag'></div>" +
        "<p class='card__date text--label'></p>" +
        "<p class='card__desc text--subtitle'></p>" +
        "</a>";

      var counter = 1;
      var numberOfSlider;

      if ($(window).width() < 1025) {
        numberOfSlider = 1;
        $(".result__cards .swiper-wrapper")
        .addClass("mobile")
        .removeClass("desktop");
      } else {
        $(".result__cards .swiper-wrapper")
        .addClass("desktop")
        .removeClass("mobile");
        numberOfSlider = 4;
      }
      data.map(function (cardData) {
        if (counter <= numberOfSlider) {
          if (counter == 1) {
            $(".relatedHashtag__result .swiper-wrapper").append(
              "<div class='swiper-slide'><div class='cards'></div></div>"
            );
          }
          $(".relatedHashtag__result .swiper-wrapper")
            .find(".cards")
            .last()
            .append(emptyCard);

          var lastCard = $(
            ".relatedHashtag__result .swiper-wrapper .card"
          ).last();

          lastCard.attr("href", cardData.link);

          lastCard
            .find(".card__bg")
            .attr("data-bg", cardData.image)
            .attr("data-bg-mobile", cardData.image);

          addBg($(window).width());

          lastCard.find(".card__hashtag").text("#" + cardData.hashtag);

          lastCard.find(".card__date").text(cardData.date);

          lastCard.find(".card__desc").text(cardData.text);

          counter = counter + 1;
          if (counter > numberOfSlider) {
            counter = 1;
          }
        }
      });
    } else {
      $(".result__number .number").text(0);
    }
  }

  function getResult(parm, max) {
    // Remove previus html and slider
    if ($(".relatedHashtag .swiper-slide-active").length > 0) {
      $(".relatedHashtag .swiper-container")[0].swiper.destroy();
      addBg($(window).width());
    }
    $(".relatedHashtag__result .swiper-slide").remove();

    $.ajax({
      url: `/bin/hashtags?tag=${parm}&max=${max}`,
      type: "GET",
      success: function (res) {
        dataHashtag = res;
      },
      complete: function () {  
        setSwiperStructure(dataHashtag);
        cardsSwiper();
      },
    });
  }
});
