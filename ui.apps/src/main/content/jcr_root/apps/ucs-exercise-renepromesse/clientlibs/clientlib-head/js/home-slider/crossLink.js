$(document).ready(function () {
  crossLinkSlider();
  crossResize();
  oneSliderFix($(".heroBanner-welcome"), heroBannerSwiper);
});

$(window).on("resize", function () {
  crossLinkSlider();
  crossResize();
  oneSliderFix($(".heroBanner-welcome"), heroBannerSwiper);
});

var crossLinkSlider = function () {
  $(".crossLink.crossLink__secondLayout .crossLink__container").each(
    function () {
      var swiperName = $(this).attr("data-swiper-name");
      $(this).addClass(swiperName);

      var w = $(window).width();

      if (w <= 1024) {
        if ($("." + swiperName + " .swiper-slide-active").length > 0) {
          // destroy and initialize again
          $("." + swiperName + ".crossLink__container")[0].swiper.destroy();
        }

        $("." + swiperName + " .swiper-slide").css("max-width", w - 30);
        $("." + swiperName + ".crossLink__container").css("max-width", w - 30);

        crossLinkSwiper = new Swiper(
          "." + swiperName + ".crossLink__container",
          {
            spaceBetween: 10,
            paginationClickable: true,
            autoResize: true,
            pagination: {
              el: "." + swiperName + " [data-pagination]",
              clickable: true,
            },
          }
        );
        var totalSlide = $(
          ".heroBanner .swiper-slide:not(.swiper-slide-duplicate)"
        ).length;

        if (totalSlide == 1) {
          $(this).find("[data-pagination]").hide();
          crossLinkSwiper.params.simulateTouch = false;
        } else {
          $(this).find("[data-pagination]").show();
          crossLinkSwiper.params.simulateTouch = true;
        }
      }
    }
  );
};

var crossResize = function () {
  var w = $(window).width();

  if (w > 1024) {
    $(".crossLink").each(function () {
      var position = $(this).attr("data-position");
      $(this)
        .find(".swiper-container")
        .css({ "margin-left": 0, "margin-right": 0 });
      if (position == "left") {
        $(this)
          .find(".swiper-wrapper")
          .css({ display: "flex", "justify-content": "flex-start" });
      } else if (position == "center") {
        $(this)
          .find(".swiper-wrapper")
          .css({ display: "flex", "justify-content": "center" });
      } else if (position == "right") {
        $(this)
          .find(".swiper-wrapper")
          .css({ display: "flex", "justify-content": "flex-end" });
      }
    });
  }
};
