<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Contact Us | Flavouriz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        

        /* HERO BANNER */
        .hero {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .hero h1 {
            color: white;
            font-size: 50px;
            background-color: rgba(0, 0, 0, 0.5);
            padding: 20px 40px;
            border-radius: 10px;
        }

        /* MAIN SECTION */
        .main-contact {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            padding: 50px 5%;
            gap: 40px;
        }

        .contact-info,
        .contact-form {
            flex: 1 1 45%;
        }

        .contact-info h1,
        .contact-form h1 {
            margin-bottom: 20px;
            font-size: 32px;
        }

        .contact-info p {
            font-size: 18px;
            line-height: 1.6;
            color: #333;
        }

        .contact-form form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .row {
            display: flex;
            gap: 15px;
        }

        .row input {
            flex: 1;
        }

        input, textarea {
            padding: 12px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 100%;
        }

        textarea {
            resize: vertical;
            min-height: 100px;
        }

        button {
            background-color: black;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 5px;
            width: 120px;
            cursor: pointer;
        }

        button:hover {
            background-color: #333;
        }

        /* MAP SECTION */
        .map {
            padding: 0 5%;
            margin-top: 30px;
        }

        .map iframe {
            width: 100%;
            height: 400px;
            border: none;
            border-radius: 10px;
        }

        /* SECOND BANNER SECTION */
        .banner-cta {
            margin: 60px 0;
            height: 250px;
            background: url('second-banner.jpg') no-repeat center center/cover;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .banner-cta button {
            font-size: 20px;
            padding: 15px 30px;
        }
    </style>
</head>
<body>

    <!-- Header -->
    <jsp:include page="header.jsp" />

    <!-- Hero Banner -->
    <section class="hero">
        <img src="${pageContext.request.contextPath}/images/cooking-banner-7166200_1280.jpg" alt="Cooking Image" style="width: 100%; height: 300px; object-fit: cover;">
    </section>
    
    <!-- Contact Section -->
    <section class="main-contact">
        <!-- Left -->
        <div class="contact-info">
            <h1>We’d love to hear from you!</h1>
            <p>
                Whether you have a question about our recipes, need help with the site, or just want to share your love for food — we’re here to listen.
                Reach out through this form and our team will get back to you as soon as possible.
                <br><br>
                You can also find us on social media, or drop by our office in Kathmandu!
            </p>
        </div>

        <!-- Right -->
        <div class="contact-form">
            <h1>Send a Message</h1>
            <form action="#" method="post">
                <div class="row">
                    <input type="text" placeholder="Your Name" required>
                    <input type="email" placeholder="Your Email" required>
                </div>
                <input type="text" placeholder="Subject" required>
                <textarea placeholder="Your Message" required></textarea>
                <button type="submit">Send</button>
            </form>
        </div>
    </section>

    <!-- Map Section -->
    <section class="map">
        <iframe 
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3532.428347232434!2d85.3123296743735!3d27.7034532266718!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x39eb198aa0a92ef5%3A0xe72df3006c4e82c4!2sGongabu%2C%20Kathmandu!5e0!3m2!1sen!2snp!4v1685805575436!5m2!1sen!2snp" 
            allowfullscreen="" 
            loading="lazy" 
            referrerpolicy="no-referrer-when-downgrade">
        </iframe>
    </section>

    <!-- CTA Banner -->
    <section class="banner-cta">
        <button ><a href="${pageContext.request.contextPath}/search">Browse Recipes</a></button>
    </section>

    <!-- Footer -->
    <jsp:include page="footer.jsp" />
    
</body>
</html>