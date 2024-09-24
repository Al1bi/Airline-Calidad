$(document).ready(function() {
    let searchActive = false;
    initSearchForm();
    initSearch();

function initSearch() {
    if($('.search_tab').length)
        {
            $('.search_tab').on('click', function()
            {
                $('.search_tab').removeClass('active');
                $(this).addClass('active');
                let clickedIndex = $('.search_tab').index(this);

                let panels = $('.search_panel');
                panels.removeClass('active');
                $(panels[clickedIndex]).addClass('active');
            });
        }
    }
    function initSearchForm()
    {
        if($('.search_form').length)
        {
            let searchForm = $('.search_form');
            let searchButton = $('.content_search');

            searchButton.on('click', function(event)
            {
                event.stopPropagation();

                if(!searchActive)
                {
                    searchForm.addClass('active');
                    searchActive = true;

                    $(document).one('click', function closeForm(e)
                    {
                        if($(e.target).hasClass('search_content_input'))
                        {
                            $(document).one('click', closeForm);
                        }
                        else
                        {
                            searchForm.removeClass('active');
                            searchActive = false;
                        }
                    });
                }
                else
                {
                    searchForm.removeClass('active');
                    searchActive = false;
                }
            });
        }
    }
});

window.addEventListener("DOMContentLoaded", function(e) {
    let links = document.getElementsByTagName("A");
    for(let i=0; i < links.length; i++) {
        if(!links[i].hash) continue;
        if(links[i].origin + links[i].pathname != self.location.href) continue;
        (function(anchorPoint) {
            links[i].addEventListener("click", function(e) {
                anchorPoint.scrollIntoView(true);
                e.preventDefault();
            }, false);
        })(document.getElementById(links[i].hash.replace(/#/, "")));
    }
}, false);