const stars = document.querySelectorAll(".rating .star");
const ratingValue = document.getElementById("rating-value");

let currentRating = 0;

stars.forEach((star) => {
    star.addEventListener("mouseover", () => {
        resetStars();
        const rating = parseInt(star.getAttribute("data-rating"));
        highlightStars(rating);
    });

    star.addEventListener("mouseout", () => {
        resetStars();
        highlightStars(currentRating);
    });

    star.addEventListener("click", () => {
        currentRating = parseInt(star.getAttribute("data-rating"));
        ratingValue.textContent = `Bạn đã đánh giá ${currentRating} sao.`;
        highlightStars(currentRating);
    });
});

function resetStars() {
    stars.forEach((star) => {
        star.classList.remove("active");
    });
}

function highlightStars(rating) {
    stars.forEach((star) => {
        const starRating = parseInt(star.getAttribute("data-rating"));
        if (starRating <= rating) {
            star.classList.add("active");
        }
    });
}

// Hien thi danh sach review
const reviewListEl = document.querySelector(".review-list");
const renderReviews = reviews => {
    let html = "";
    const currentUser = sessionStorage.getItem("CURRENT_USER");
    reviews.forEach(review => {
        const isCurrentUser = currentUser && currentUser.id === review.user.id;
        html += `
            <div class="rating-item d-flex align-items-center mb-3 pb-3">
                <div class="rating-avatar">
                    <img src="${review.user.avatar}" alt="${review.user.name}">
                </div>
                <div class="rating-info ms-3">
                    <div class="d-flex align-items-center">
                        <p class="rating-name mb-0"><strong>${review.user.name}</strong></p>
                        <p class="rating-time mb-0 ms-2">${formatDate(review.createdAt)}</p>
                    </div>
                    <div class="rating-star">
                        <p class="mb-0 fw-medium">
                            <span class="rating-icon me-1"><i class="fa fa-star"></i></span>
                            <span>${review.rating}/10</span>
                        </p>
                    </div>
                    <p class="rating-content mt-1 mb-0 text-muted">${review.content}</p>
                       <div ${!isCurrentUser ? 'style="display:none;"' : ''}>
                        <button onclick="openModalUpdateReview(${review.id})"
                            class="text-primary border-0 bg-transparent text-decoration-underline me-1">Sửa
                        </button>
                        <button onclick="deleteReview(${review.id})"
                                class="text-danger border-0 bg-transparent text-decoration-underline me-1">Xóa
                        </button>
                    </div>
                </div>
            </div>
        `
    });
    reviewListEl.innerHTML = html;
}

const render = () => {
    $('#review-pagination').pagination({
        dataSource: reviews,
        pageSize: 5,
        callback: function(data, pagination) {
            renderReviews(data);
        }
    })
}

const formatDate = dateString => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ("0" + (date.getMonth() + 1)).slice(-2); // 09 -> 09 , 011 -> 11
    const day = ("0" + date.getDate()).slice(-2);
    return `${day}/${month}/${year}`;
}

const formReviewEl = document.getElementById("form-review");
const reviewContentEl = document.getElementById("review-content");
const modalReviewEl = document.getElementById('modalReview');
const titleModalReviewEl = document.querySelector('#modalReview .modal-title');
const btnSubmitReviewEl = document.getElementById("btn-submit");
const modalReviewObj = new bootstrap.Modal('#modalReview', {
    keyboard: false
})
let idUpdate = null;

modalReviewEl.addEventListener('hidden.bs.modal', event => {
    resetStars();
    currentRating = 0;
    ratingValue.innerHTML = "Vui lòng chọn đánh giá";
    reviewContentEl.value = "";
    titleModalReviewEl.innerHTML = "Tạo bình luận";
    btnSubmitReviewEl.innerHTML = "Tạo bình luận";
    idUpdate = null;
})

const openModalUpdateReview = (id) => {
    const review = reviews.find(review => review.id === id);
    currentRating = review.rating;
    highlightStars(currentRating);
    ratingValue.innerHTML = `Bạn đã đánh giá ${currentRating} sao.`;
    reviewContentEl.value = review.content;
    titleModalReviewEl.innerHTML = "Cập nhật bình luận";
    btnSubmitReviewEl.innerHTML = "Cập nhật bình luận";
    modalReviewObj.show();
    idUpdate = id;
};

formReviewEl.addEventListener("submit", (e) => {
    e.preventDefault();
    if (idUpdate) {
        updateReview();
    } else {
        createReview();
    }
})

// Tạo review
const createReview = async () => {
if (!sessionStorage.getItem("CURRENT_USER")) {
        toastr.warning("Vui lòng đăng nhập để bình luận");
        return;
}
    if (currentRating === 0) {
        toastr.warning("Vui lòng chọn số sao");
        return;
    }

    if (reviewContentEl.value.trim() === "") {
        toastr.warning("Vui lòng nhập nội dung bình luận");
        return;
    }

    const request = {
        rating: currentRating,
        content: reviewContentEl.value,
        movieId: movie.id
    }

    try {
        let res = await axios.post("/api/reviews", request);
        console.log(res.data)
        reviews.unshift(res.data);
        render(reviews);
        modalReviewObj.hide();
        toastr.success("Tạo bình luận thành công");
    } catch (error) {
        console.log(error);
        toastr.error(error.response.data.message);
    }
}

// Cập nhật review
const updateReview = async () => {
    if (currentRating === 0) {
        toastr.warning("Vui lòng chọn số sao");
        return;
    }

    if (reviewContentEl.value.trim() === "") {
        toastr.warning("Vui lòng nhập nội dung bình luận");
        return;
    }

    const request = {
        rating: currentRating,
        content: reviewContentEl.value,
    }

    try {
        let res = await axios.put(`/api/reviews/${idUpdate}`, request);
        const index = reviews.findIndex(review => review.id === idUpdate);
        reviews[index] = res.data;
        render(reviews);
        modalReviewObj.hide();
        toastr.success("Cập nhật bình luận thành công");
    } catch (error) {
        console.log(error);
        toastr.error(error.response.data.message);
    }
}

// Xóa review
const deleteReview = async (id) => {
    const isConfirm = confirm("Bạn có chắc chắn muốn xóa bình luận này không?");
    if (!isConfirm) return;

    try {
        let res = await axios.delete(`/api/reviews/${id}`);
        reviews = reviews.filter(review => review.id !== id);
        render(reviews);
        toastr.success("Xóa bình luận thành công");
    } catch (error) {
        console.log(error);
        toastr.error(error.response.data.message);
    }
}

render();